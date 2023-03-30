package ch.bailu.gtk.model

import ch.bailu.gtk.Configuration
import ch.bailu.gtk.converter.NamespaceType
import ch.bailu.gtk.model.compose.CodeComposer
import ch.bailu.gtk.model.filter.*
import ch.bailu.gtk.model.list.ModelLists
import ch.bailu.gtk.model.type.StructureType
import ch.bailu.gtk.parser.tag.*
import ch.bailu.gtk.table.AliasTable
import ch.bailu.gtk.validator.Validator
import ch.bailu.gtk.writer.CodeWriter
import ch.bailu.gtk.writer.Names
import java.io.IOException


class StructureModel : Model {
    val apiName: String
    val nameSpaceModel: NamespaceModel

    private var parent: StructureModel
    private var models = ModelLists()

    val structureType: StructureType
    val typeFunction: String

    val cType: String
    val doc: String

    val disguised: Boolean

    val hasGetTypeFunction: Boolean
        get() = "" != typeFunction

    /**
     * Offset and field order must be identical with c structure
     */
    var allFieldsAreSupported = true
        private set

    constructor(structure: StructureTag, nameSpace: NamespaceModel) {
        typeFunction = structure.getType

        disguised = structure.disguised
        cType = structure.type
        nameSpaceModel = nameSpace
        structureType = StructureType(structure.structureType)
        apiName = AliasTable.convert(nameSpace.namespace, structure.getName()).name
        parent = StructureModel(nameSpace.namespace, structure.parent, structureType)
        doc = structure.getDoc()

        structure.constructors.forEach {
            generateAndAddMethodModel(nameSpace, models.privateFactories, it)
        }

        models.privateFactories.forEach {
            if (it.isConstructorType) {
                models.constructors.add(it)
            } else {
                models.factories.add(it)
            }
        }

        structure.methods.forEach {
            generateAndAddMethodModel(nameSpace, models.methods, it)
        }

        structure.signals.forEach {
            models.signals.addIfSupported(MethodModel(nameSpace.namespace,nameSpace.namespace, it, preferNative = false))
        }

        structure.fields.forEach {
            generateAndAddFieldModel(nameSpace, it)
        }

        structure.functions.forEach {
            models.addIfSupportedWithCallbacks(models.functions, filter(MethodModel(nameSpace.namespace, nameSpace.namespace, it, preferNative = false)))
        }

        structure.implements.forEach {
            models.implements.addIfSupported(ImplementsModel(nameSpace.namespace, it))
        }

        setSupported("name-is-empty", apiName != "")
    }

    private fun generateAndAddFieldModel(namespace: NamespaceModel, fieldTag: FieldTag) {
        val fieldModel = filterField(FieldModel(
            namespace.namespace,
            fieldTag, supportsDirectAccess = filterFieldDirectAccess()
        ))

        fieldModel.setSupported("previous-field-unsupported", allFieldsAreSupported)

        models.fields.addIfSupported(fieldModel)
        if (fieldModel.isMethod) {
            models.callbacks.addIfSupported(fieldModel.methodModel, fieldModel.isSupported)
        }

        allFieldsAreSupported = allFieldsAreSupported && fieldModel.isSupported
    }

    private fun generateAndAddMethodModel(namespace: NamespaceModel, models: ModelList<MethodModel>, methodTag: MethodTag) {
        val methodModel = MethodModel(namespace.namespace,namespace.namespace, methodTag, preferNative = false)
        this.models.addIfSupportedWithCallbacks(models, filterConstructor(methodModel))

        if (methodModel.isSupported && methodModel.hasNativeVariant) {
            val methodModelNativeOverload = MethodModel(namespace.namespace,namespace.namespace, methodTag, preferNative = true)
            this.models.addIfSupportedWithCallbacks(models, filterConstructor(methodModelNativeOverload))
        }
    }

    private fun filterField(fieldModel: FieldModel): FieldModel {
        fieldModel.setSupported("filter-field", filterField())
        return fieldModel
    }

    private fun filterConstructor(methodModel: MethodModel): MethodModel {
        return filter(methodModel)
    }

    private fun filter(methodModel: MethodModel): MethodModel {
        methodModel.setSupported("method-filter", filterMethod(this, methodModel))
        return methodModel
    }

    /**
     * Gets called from builder when namespace ends
     * Create static class for package scoped functions
     * @param namespace
     */
    constructor(namespace: NamespaceModel) {
        disguised = false
        typeFunction = ""
        doc=""
        cType = ""
        nameSpaceModel = namespace
        structureType = StructureType(StructureType.Types.PACKAGE)
        apiName = Names.getJavaClassName(nameSpaceModel.namespace)
        parent = StructureModel(nameSpaceModel.namespace, "", structureType)
        for (m in namespace.functions) {
            // TODO support preferred natives
            models.addIfSupportedWithCallbacks(models.functions, filter(MethodModel(nameSpaceModel.namespace, nameSpaceModel.namespace,m, preferNative = false)))
        }
    }

    /**
     * This gets called from builder when namespace ends
     * Create interface with package scoped constants
     * @param namespace
     * @param members
     */
    constructor(namespace: NamespaceModel, members: List<ParameterTag>) :
            this(namespace, Names.getJavaPackageConstantsInterfaceName(namespace.namespace), members, toUpper = false)

    /**
     * Gets called from ModelBuilder
     * Create interface with constants
     * @param namespace
     * @param enumeration
     */
    constructor(namespace: NamespaceModel, enumeration: EnumerationTag) : this(namespace, enumeration.getName(), enumeration.getMembers(), true)

    /**
     * Create interface with constants
     * @param namespace
     * @param name
     * @param members
     */
    private constructor(namespace: NamespaceModel, name: String, members: List<ParameterTag>, toUpper: Boolean) {
        typeFunction = ""
        nameSpaceModel = namespace
        structureType = StructureType(StructureType.Types.ENUMERATION)
        apiName = name
        doc = ""
        cType = ""
        disguised = false
        parent = this

        for (parameterTag in members) {
            models.constants.addIfSupported(
                ParameterModel(namespace.namespace, parameterTag,
                    isConstant = toUpper,
                    preferNative = true,
                    supportsDirectAccess = false
                )
            )
        }
    }

    // parent initializer
    private constructor(defaultNamespace: String, className: String, structType: StructureType) {
        typeFunction = ""
        doc = ""
        cType = ""
        structureType = structType
        disguised = false
        parent = this

        if (className == "") {
            nameSpaceModel = NamespaceModel()
            apiName = structType.apiParentClassName

        } else {
            val type = AliasTable.convert(NamespaceType(defaultNamespace, className))
            val typeNamespaceModel = NamespaceModel(type)
            if (typeNamespaceModel.isSupported) {
                nameSpaceModel = typeNamespaceModel
                apiName = if (type.isCurrentNameSpace(nameSpaceModel.namespace)) {
                    type.name
                } else {
                    nameSpaceModel.fullNamespace + "." + type.name
                }
            } else {
                nameSpaceModel = NamespaceModel()
                apiName = Configuration.BASE_NAME_SPACE_DOT + "type.Outsider"
            }
        }

        Validator.validateAlias(apiName)
    }

    fun hasNativeCalls(): Boolean {
        if (isSupported) {
            if (structureType.isRecord && filterCreateMallocConstructor(this)) {
                return true
            } else if (structureType.isPackage || structureType.isClassType) {
                return models.hasNativeCalls() || hasGetTypeFunction
            }
        }
        return false
    }

    @Throws(IOException::class)
    fun write(writer: CodeWriter) {
        CodeComposer.factory(structureType).write(writer, nameSpaceModel, this, models)
    }


    val isRecord: Boolean
        get() = structureType.isRecord

    val jnaName: String
        get() = Names.getImpClassName(apiName)

    val apiParentName: String
        get() = parent.getApiTypeName(nameSpaceModel.namespace)

     fun hasDefaultConstructor(): Boolean {
         return models.constructors.find { return it.parameters.isEmpty() } != null
     }

    fun getApiTypeName(namespace: String): String {
        return if (nameSpaceModel.namespace != namespace) {
            Names.getJavaClassNameWithNamespacePrefix(nameSpaceModel.namespace, apiName)
        } else {
            apiName
        }
    }
}
