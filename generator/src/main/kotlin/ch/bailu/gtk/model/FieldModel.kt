package ch.bailu.gtk.model

import ch.bailu.gtk.log.DebugPrint
import ch.bailu.gtk.model.type.CallbackType
import ch.bailu.gtk.model.type.ClassType
import ch.bailu.gtk.model.type.JavaType
import ch.bailu.gtk.model.type.NamespaceType
import ch.bailu.gtk.parser.tag.CallbackTag
import ch.bailu.gtk.parser.tag.FieldTag
import ch.bailu.gtk.parser.tag.MethodTag
import ch.bailu.gtk.table.EnumTable
import ch.bailu.gtk.table.SizeTable
import ch.bailu.gtk.table.StructureTable
import ch.bailu.gtk.writer.Names

class FieldModel(namespace: String, fieldTag: FieldTag) : Model() {
    val classType = ClassType(namespace, fieldTag)
    private val jType: JavaType
    private val hasNativeVariant: Boolean
    val methodModel: MethodModel

    val isMethod: Boolean
    val isDirectType: Boolean
    val isJavaNative: Boolean

    val doc = fieldTag.getDoc()
    val name = Names.getJavaVariableName(fieldTag.getName())
    val impType: String
    val isWriteable: Boolean


    init {
        val callbackType = CallbackType(namespace, fieldTag.getTypeName())

        if (!classType.valid && EnumTable.isEnum(namespace, fieldTag)) {
            this.jType = JavaType("int")
            hasNativeVariant = false

        } else {
            val jType = JavaType(fieldTag.getType())
            hasNativeVariant = classType.valid && jType.valid

            if (classType.valid) {
                this.jType = JavaType("long")

            } else {
                this.jType = jType
            }
        }

        methodModel = MethodModel(namespace, namespace, getMethodTag(fieldTag.isMethod, fieldTag.method, callbackType.callbackTag), preferNative = false)
        isMethod = (fieldTag.isMethod || callbackType.valid) && methodModel.isSupported
        impType = jType.getApiTypeName()

        if (isMethod) {
            methodModel.setPublic("cb-with-cb", !methodModel.hasCallback())
            setPublic(methodModel.visibleState, methodModel.isPublic)

        } else {
            setSupported("java-type-not-supported", jType.valid)
            setSupported("direct-type", classType.referenceType || isSizeKnown(classType.type))
            setPrivate("direct-type", classType.directType)
        }
        isDirectType = classType.directType
        isWriteable = fieldTag.isWriteable || isMethod
        isJavaNative = !isMethod && !classType.valid
    }

    private fun isSizeKnown(namespaceType: NamespaceType): Boolean {
        return StructureTable.isSizeKnown(namespaceType) || SizeTable.isSizeKnown(namespaceType)
    }

    private fun getMethodTag(isMethod: Boolean, methodTag: MethodTag, callbackTag: CallbackTag?): MethodTag {
        return if (!isMethod && callbackTag is CallbackTag) {
            callbackTag
        } else {
            methodTag
        }
    }

    fun getApiTypeName(namespace: String): String {
        return if (isMethod) {
            Names.getJavaCallbackInterfaceName(methodModel.name)
        } else if (classType.valid) {
            classType.getApiTypeName(namespace)
        } else { // native
            jType.getApiTypeName()
        }
    }

    override fun toString(): String {
        if (isMethod) {
            return DebugPrint.colon(this, name, supportedState, methodModel.toString())
        }
        return DebugPrint.colon(this, name, supportedState, classType.toString(), jType.toString())
    }
}
