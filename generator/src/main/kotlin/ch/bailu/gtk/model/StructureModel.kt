package ch.bailu.gtk.model

import ch.bailu.gtk.converter.NamespaceType
import ch.bailu.gtk.model.compose.CodeComposer
import ch.bailu.gtk.model.filter.*
import ch.bailu.gtk.model.list.ModelLists
import ch.bailu.gtk.model.type.StructureType
import ch.bailu.gtk.parser.tag.EnumerationTag
import ch.bailu.gtk.parser.tag.MethodTag
import ch.bailu.gtk.parser.tag.ParameterTag
import ch.bailu.gtk.parser.tag.StructureTag
import ch.bailu.gtk.table.AliasTable.convert
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

    val hasGetTypeFunction: Boolean
        get() = "" != typeFunction


    constructor(structure: StructureTag, nameSpace: NamespaceModel) {
        typeFunction = structure.getType

        cType = structure.type
        nameSpaceModel = nameSpace
        structureType = StructureType(structure.structureType)
        apiName = convert(nameSpace.namespace, structure.getName())
        parent = StructureModel(nameSpace.namespace, structure.parent, structureType)
        doc = structure.getDoc()
        for (methodTag in structure.constructors) {
            generateAndAddMethodModel(nameSpace, models.privateFactories, methodTag)
        }

        models.privateFactories.forEach {
            if (it.isConstructorType) {
                models.constructors.add(it)
            } else {
                models.factories.add(it)
            }
        }

        for (methodTag in structure.methods) {
            generateAndAddMethodModel(nameSpace, models.methods, methodTag)
        }

        for (signal in structure.signals) {
            models.signals.addIfSupported(MethodModel(nameSpace.namespace,nameSpace.namespace, signal, preferNative = false))
        }
        for (field in structure.fields) {
            val fieldModel = ParameterModel(
                nameSpace.namespace,
                field, isConstant = false,
                supportsDirectAccess = filterFieldDirectAccess(this),
                preferNative = false)
            models.fields.addIfSupported(filterField(fieldModel))
        }
        for (m in structure.functions) {
            models.addIfSupportedWithCallbacks(models.functions, filter(MethodModel(nameSpace.namespace, nameSpace.namespace,m, preferNative = false)))
        }

        setSupported("name-is-empty", apiName != "")
    }

    private fun generateAndAddMethodModel(namespace: NamespaceModel, models: ModelList<MethodModel>, methodTag: MethodTag) {
        val methodModel = MethodModel(namespace.namespace,namespace.namespace, methodTag, preferNative = false)
        this.models.addIfSupportedWithCallbacks(models, filterConstructor(methodModel))

        if (methodModel.isSupported && methodModel.hasNativeVariant) {
            val methodModelNativeOverload = MethodModel(namespace.namespace,namespace.namespace, methodTag, preferNative = true)
            this.models.addIfSupportedWithCallbacks(models, filterConstructor(methodModelNativeOverload))
        }
    }

    private fun convert(namespace: String, name: String): String {
        val from = NamespaceType(namespace, name)
        return convert(from).name
    }

    private fun filterField(parameterModel: ParameterModel): ParameterModel {
        parameterModel.setSupported("cb-field", !parameterModel.isCallback)
        parameterModel.setSupported("filter-field", filterField(this))
        return parameterModel
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
        parent = this

        if (className == "") {
            nameSpaceModel = NamespaceModel()
            apiName = when {
                structType.isRecord -> {
                    nameSpaceModel.fullNamespace + ".type.Record"
                }
                structType.isPackage -> {
                    nameSpaceModel.fullNamespace + ".type.Package"
                }
                structType.isCallback -> {
                    nameSpaceModel.fullNamespace + ".type.Callback"
                }
                else -> {
                    nameSpaceModel.fullNamespace + ".type.Pointer"
                }
            }
        } else {
            val type = NamespaceType(defaultNamespace, className)
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
                apiName = nameSpaceModel.fullNamespace + ".type.Outsider"
            }
        }
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
