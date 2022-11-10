package ch.bailu.gtk.model

import ch.bailu.gtk.converter.NamespaceType
import ch.bailu.gtk.converter.RelativeNamespaceType
import ch.bailu.gtk.model.compose.CodeComposer
import ch.bailu.gtk.model.filter.*
import ch.bailu.gtk.model.list.ModelLists
import ch.bailu.gtk.model.type.StructureType
import ch.bailu.gtk.parser.tag.*
import ch.bailu.gtk.table.AliasTable.convert
import ch.bailu.gtk.writer.CodeWriter
import ch.bailu.gtk.writer.getJavaClassName
import ch.bailu.gtk.writer.getJavaPackageConstantsInterfaceName
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
        for (m in structure.constructors) {
            val methodModel = MethodModel(nameSpace.namespace, m, preferNative = false)
            models.addIfSupportedWithCallbacks(models.privateFactories, filterConstructor(methodModel))

            if (methodModel.isSupported && methodModel.hasNativeVariant) {
                val methodModel = MethodModel(nameSpace.namespace, m, preferNative = true)
                models.addIfSupportedWithCallbacks(models.privateFactories, filterConstructor(methodModel))
            }
        }

        models.privateFactories.forEach {
            if (it.isConstructorType) {
                models.constructors.add(it)
            } else {
                models.factories.add(it)
            }
        }

        for (method in structure.methods) {
            val methodModel = MethodModel(nameSpace.namespace, method, preferNative = false)
            models.addIfSupportedWithCallbacks(models.methods, filter(methodModel))

            if (methodModel.isSupported && methodModel.hasNativeVariant) {
                val methodModel = MethodModel(nameSpace.namespace, method, preferNative = true)
                models.addIfSupportedWithCallbacks(models.methods, filter(methodModel))
            }
        }

        for (signal in structure.signals) {
            models.signals.addIfSupported(MethodModel(nameSpace.namespace, signal, preferNative = false))
        }
        for (field in structure.fields) {
            val fieldModel = ParameterModel(
                nameSpace.namespace,
                field, toUpper = false,
                supportsDirectAccess = filterFieldDirectAccess(this),
                preferNative = false)
            models.fields.addIfSupported(filterField(fieldModel))
        }
        for (m in structure.functions) {
            models.addIfSupportedWithCallbacks(models.functions, filter(MethodModel(nameSpace.namespace, m, preferNative = false)))
        }

        setSupported("name", apiName != "")
    }

    private fun convert(namespace: String, name: String): String {
        val from = NamespaceType(namespace, name)
        return convert(from).name
    }

    private fun filterField(parameterModel: ParameterModel): ParameterModel {
        parameterModel.setSupported("cb-field", !parameterModel.isCallback)
        parameterModel.setSupported("filter", filterField(this))
        return parameterModel
    }

    private fun filterConstructor(methodModel: MethodModel): MethodModel {
        //methodModel.setSupported("cb-constructor", !methodModel.hasCallback())
        return filter(methodModel)
    }

    private fun filter(methodModel: MethodModel): MethodModel {
        methodModel.setSupported("filter", filterMethod(this, methodModel))
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
        apiName = getJavaClassName(nameSpaceModel.namespace)
        parent = StructureModel(nameSpaceModel.namespace, "", structureType)
        for (m in namespace.functions) {
            // TODO support preferred natives
            models.addIfSupportedWithCallbacks(models.functions, filter(MethodModel(nameSpaceModel.namespace, m, preferNative = false)))
        }
    }

    /**
     * This gets called from builder when namespace ends
     * Create interface with package scoped constants
     * @param namespace
     * @param members
     */
    constructor(namespace: NamespaceModel, members: List<ParameterTag>) : this(namespace, getJavaPackageConstantsInterfaceName(namespace.namespace), members, false)

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
                    toUpper = toUpper,
                    preferNative = false,
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
            val type = RelativeNamespaceType(defaultNamespace, className)
            val typeNamespaceModel = NamespaceModel(type)
            if (typeNamespaceModel.isSupported) {
                nameSpaceModel = typeNamespaceModel
                apiName = if (type.hasCurrentNamespace()) {
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
        get() = "Jna${apiName}"

    val impName: String
        get() = "Imp${apiName}"

    val apiParentName: String
        get() = parent.apiName


    fun hasDefaultConstructor(): Boolean {
        return models.constructors.find { return it.parameters.isEmpty() } != null
    }
}
