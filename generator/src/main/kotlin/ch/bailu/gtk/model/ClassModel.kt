package ch.bailu.gtk.model

import ch.bailu.gtk.converter.NamespaceType
import ch.bailu.gtk.converter.RelativeNamespaceType
import ch.bailu.gtk.model.filter.*
import ch.bailu.gtk.table.AliasTable.convert
import ch.bailu.gtk.tag.EnumerationTag
import ch.bailu.gtk.tag.NamespaceTag
import ch.bailu.gtk.tag.ParameterTag
import ch.bailu.gtk.tag.StructureTag
import ch.bailu.gtk.writer.CodeWriter
import ch.bailu.gtk.writer.getJavaClassName
import ch.bailu.gtk.writer.getJavaImpClassName
import ch.bailu.gtk.writer.getJavaPackageConstantsInterfaceName
import java.io.IOException


class ClassModel : Model {
    val apiName: String
    val nameSpaceModel: NamespaceModel
    
    private var parent: ClassModel
    private val unsupported: ModelList<Model> = ModelList()
    private val privateFactories = ModelList<MethodModel>(unsupported)
    private val factories = ModelList<MethodModel>(unsupported)
    private val constructors = ModelList<MethodModel>(unsupported)
    private val methods = ModelList<MethodModel>(unsupported)
    private val signals = ModelList<MethodModel>(unsupported)
    private val callbacks = ModelList<MethodModel>(unsupported)
    private val functions = ModelList<MethodModel>(unsupported)
    private val fields = ModelList<ParameterModel>(unsupported)
    private val constants = ModelList<ParameterModel>(unsupported)

    // record, enum, class, interface, bitfield, callback
    private val structureType: String
    
    val cType: String
    val doc: String

    constructor(structure: StructureTag, nameSpace: NamespaceModel) {
        cType = structure.getType()
        nameSpaceModel = nameSpace
        structureType = structure.getStructureType()
        apiName = convert(nameSpace.getNamespace(), structure.getName())
        parent = ClassModel(nameSpace.getNamespace(), structure.getParentName(), structureType)
        doc = structure.getDoc()
        for (m in structure.getConstructors()) {
            privateFactories.addIfSupported(filterConstructor(MethodModel(nameSpace.getNamespace(), m)))
        }
        for (factory in privateFactories) {
            if (factory.isConstructorType) {
                constructors.add(factory)
            } else {
                factories.add(factory)
            }
        }
        for (method in structure.getMethods()) {
            addIfSupportedWithCallbacks(methods, filter(MethodModel(nameSpace.getNamespace(), method)))
        }
        for (signal in structure.getSignals()) {
            signals.addIfSupported(MethodModel(nameSpace.getNamespace(), signal))
        }
        for (field in structure.getFields()) {
            val fieldModel = ParameterModel(nameSpace.getNamespace(), field, false, filterFieldDirectAccess(this))
            fields.addIfSupported(filterField(fieldModel))
        }
        for (m in structure.getFunctions()) {
            addIfSupportedWithCallbacks(functions, filter(MethodModel(nameSpace.getNamespace(), m)))
        }
    }

    private fun convert(namespace: String, name: String): String {
        val from = NamespaceType(namespace, name)
        return convert(from).getName()
    }

    private fun filterField(parameterModel: ParameterModel): ParameterModel {
        parameterModel.setSupported("Callback", !parameterModel.isCallback)
        parameterModel.setSupported("Filter", filterField(this))
        return parameterModel
    }

    private fun filterConstructor(methodModel: MethodModel): MethodModel {
        methodModel.setSupported("Callback", !methodModel.hasCallback())
        return filter(methodModel)
    }

    private fun filter(methodModel: MethodModel): MethodModel {
        methodModel.setSupported("Filter", filterMethod(this, methodModel))
        return methodModel
    }

    /**
     * Gets called from builder when namespace ends
     * Create static class for package scoped functions
     * @param namespace
     */
    constructor(namespace: NamespaceTag) {
        doc=""
        cType = ""
        nameSpaceModel = NamespaceModel(namespace)
        structureType = "package"
        apiName = getJavaClassName(nameSpaceModel.getNamespace())
        parent = ClassModel(nameSpaceModel.getNamespace(), "", structureType)
        for (m in namespace.getFunctions()) {
            addIfSupportedWithCallbacks(functions, filter(MethodModel(nameSpaceModel.getNamespace(), m)))
        }
    }

    /**
     * This gets called from builder when namespace ends
     * Create interface with package scoped constants
     * @param namespace
     * @param members
     */
    constructor(namespace: NamespaceModel, members: List<ParameterTag>) : this(namespace, getJavaPackageConstantsInterfaceName(namespace.getNamespace()), members, false)

    /**
     * Gets called from ModelBuilder
     * Create interface with constants
     * @param namespace
     * @param enumeration
     */
    constructor(namespace: NamespaceModel, enumeration: EnumerationTag) : this(namespace, enumeration.getName(), enumeration.getMembers(), true) {}

    /**
     * Create interface with constants
     * @param namespace
     * @param name
     * @param members
     */
    private constructor(namespace: NamespaceModel, name: String, members: List<ParameterTag>, toUpper: Boolean) {
        nameSpaceModel = namespace
        structureType = "enumeration"
        apiName = name
        doc = ""
        cType = ""
        parent = this

        for (parameterTag in members) {
            constants.addIfSupported(ParameterModel(namespace.getNamespace(), parameterTag, toUpper, false))
        }
    }

    private fun addIfSupportedWithCallbacks(models: ModelList<MethodModel>, model: MethodModel) {
        models.addIfSupported(model)
        if (model.isSupported) {
            for (cb in model.getCallbackModel()) {
                if (!callbacks.contains(cb)) callbacks.add(cb)
            }
        }
    }

    // parent initializer
    private constructor(defaultNamespace: String, className: String, structType: String) {
        doc = ""
        cType = ""
        structureType = structType
        parent = this

        if (className == "") {
            nameSpaceModel = NamespaceModel()
            if ("record".equals(structType, ignoreCase = true)) {
                apiName = nameSpaceModel.getFullNamespace() + ".type.Record"
            } else if ("package".equals(structType, ignoreCase = true)) {
                apiName = nameSpaceModel.getFullNamespace() + ".type.Package"
            } else if ("callback".equals(structType, ignoreCase = true)) {
                apiName = nameSpaceModel.getFullNamespace() + ".type.Callback"
            } else {
                apiName = nameSpaceModel.getFullNamespace() + ".type.Pointer"
            }
        } else {
            val type = RelativeNamespaceType(defaultNamespace, className)
            val typeNamespaceModel = NamespaceModel(type)
            if (typeNamespaceModel.isSupported) {
                nameSpaceModel = typeNamespaceModel
                if (type.hasCurrentNamespace()) {
                    apiName = type.getName()
                } else {
                    apiName = nameSpaceModel.getFullNamespace() + "." + type.getName()
                }
            } else {
                nameSpaceModel = NamespaceModel()
                apiName = nameSpaceModel.getFullNamespace() + ".type.Outsider"
            }
        }
    }

    fun hasNativeCalls(): Boolean {
        if (isSupported) {
            if (isRecord && filterCreateMallocConstructor(this)) {
                return true
            } else if (isPackage || isClassType) {
                return methods.size > 0 || privateFactories.size > 0 || signals.size > 0 || fields.size > 0 || functions.size > 0
            }
        }
        return false
    }

    private val isClassType: Boolean
        get() = "class" == structureType || "record" == structureType || "interface" == structureType

    @Throws(IOException::class)
    fun write(writer: CodeWriter) {
        writer.writeStart(this, nameSpaceModel)
        writer.next()
        if (isClassType) {
            writer.writeClass(this)
            writer.next()
            for (cb in callbacks) {
                writer.writeCallback(this, cb)
            }
            writer.next()
            for (m in privateFactories) {
                writer.writePrivateFactory(this, m)
            }
            writer.next()
            for (m in factories) {
                writer.writeFactory(this, m)
            }
            writer.next()
            writer.writeInternalConstructor(this)
            if (isRecord && filterCreateMallocConstructor(this)) {
                writer.writeMallocConstructor(this)
            }
            writer.next()
            for (m in constructors) {
                writer.writeConstructor(this, m)
            }
            writer.next()
            for (p in fields) {
                writer.writeField(this, p)
            }
            writer.next()
            for (m in methods) {
                writer.writeNativeMethod(this, m)
            }
            writer.next()
            for (s in signals) {
                writer.writeSignal(this, s)
            }
            writer.next()
            for (m in functions) {
                writer.writeFunction(this, m)
            }
        } else if (isPackage) {
            writer.writeClass(this)
            writer.next()
            for (cb in callbacks) {
                writer.writeCallback(this, cb)
            }
            writer.next()
            for (m in functions) {
                writer.writeFunction(this, m)
            }
        } else if (isCallback) {
            writer.writeInterface(this)
            writer.next()
            for (m in functions) {
                writer.writeInterfaceMethod(this, m)
            }
        } else {
            writer.writeInterface(this)
            writer.next()
            for (p in constants) {
                writer.writeConstant(p)
            }
        }
        writer.next()
        for (m in unsupported) {
            writer.writeUnsupported(m)
        }
        writer.writeEnd()
    }

    private val isCallback: Boolean
        get() = "callback" == structureType

    private val isPackage: Boolean
        get() = "package" == structureType

    val impName: String
        get() = getJavaImpClassName(apiName)

    val apiParentName: String
        get() = parent.apiName

    val isRecord: Boolean
        get() = "record" == structureType

    fun hasDefaultConstructor(): Boolean {
        for (m in constructors) {
            if (m.getParameters().size == 0) {
                return true
            }
        }
        return false
    }
}
