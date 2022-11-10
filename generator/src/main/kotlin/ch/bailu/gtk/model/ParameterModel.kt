package ch.bailu.gtk.model

import ch.bailu.gtk.converter.isEnum
import ch.bailu.gtk.log.colonList
import ch.bailu.gtk.model.filter.filterValues
import ch.bailu.gtk.model.type.CType
import ch.bailu.gtk.model.type.ClassType
import ch.bailu.gtk.model.type.JavaType
import ch.bailu.gtk.parser.tag.ParameterTag
import ch.bailu.gtk.writer.fixToken
import ch.bailu.gtk.writer.getJavaSignalInterfaceName

class ParameterModel(namespace: String,
                     private val parameterTag: ParameterTag,
                     toUpper: Boolean,
                     supportsDirectAccess: Boolean,
                     preferNative: Boolean) : Model() {

    private val cType: CType
    private val classType: ClassType = ClassType(namespace, parameterTag, supportsDirectAccess)
    private val jType: JavaType
    val hasNativeVariant: Boolean
    val isNativeVariant: Boolean

    var name: String = if (toUpper) {
        fixToken(parameterTag.getName().uppercase())
    } else {
        fixToken(parameterTag.getName())
    }
    val isWriteable: Boolean
    val callbackModel: MethodModel?

    init {
        if (!classType.isClass() && isEnum(namespace, parameterTag)) {
            this.cType = CType("int")
            this.jType = JavaType("int")
            hasNativeVariant = false

        } else {
            val cType = CType(parameterTag.getType())
            val jType = JavaType(parameterTag.getType())
            hasNativeVariant = classType.isClass() && jType.isValid()

            if (hasNativeVariant && preferNative) {
                this.cType = cType
                this.jType = jType

            } else if (classType.isClass()) {
                this.cType = CType("void*")
                this.jType = JavaType("long")

            } else if (parameterTag.isVarargs) {
                this.cType = CType("Object...")
                this.jType = JavaType("...")
            } else {
                this.cType = cType
                this.jType = jType
            }
        }

        isNativeVariant = hasNativeVariant && preferNative

        callbackModel = createCallbackModel(classType, namespace)

        setSupported("value", filterValues(parameterTag.value))
        setSupported("jType", jType.isValid())
        setCallbackSupported()
        isWriteable = parameterTag.isWriteable
    }

    private fun createCallbackModel(classType: ClassType, namespace: String): MethodModel? {
        if (classType.isCallback()) {
            val callbackTag = classType.getCallbackTag()
            if (callbackTag != null) {
                return MethodModel(namespace, callbackTag, preferNative = false)
            }
        }
        return null
    }

    private fun setCallbackSupported() {
        if (isCallback) {
            if (callbackModel == null) {
                setSupported("no-cb-model", false)
            } else if (!callbackModel.isSupported) {
                setSupported("cb-not-supported", false)
            } else if (callbackModel.hasCallback()) {
                setSupported("cb-with-cb", false)
            }
        }
    }

    val apiType: String
        get() {
            if (isCallback && callbackModel != null) {
                return getJavaSignalInterfaceName(callbackModel.name)
            }
            return if (classType.isClass() && !isNativeVariant) {
                classType.fullName
            } else jType.getType()
        }

    val impType: String
        get() {
            return jType.getType()
        }

    val isVoid: Boolean
        get() {
            return jType.isVoid()
        }

    val isJavaNative: Boolean
        get() {
            return !classType.isClass() || isNativeVariant
        }

    val gtkType: String
        get() {
            return cType.type
        }

    override fun toString(): String {
        val supported = if (isSupported) "(s)" else ""

        return colonList(arrayOf(
                supported,
                parameterTag.getType(),
                parameterTag.getTypeName(),  // <-
                parameterTag.getName(),  // <-
                parameterTag.value,
                gtkType,
                apiType))
    }

    val isCallback: Boolean
        get() {
            return classType.isCallback()
        }

    val isDirectType: Boolean
        get() {
            return classType.isDirectType()
        }

    val value: String
        get() {
            return parameterTag.value
        }

    val doc: String
        get() = parameterTag.getDoc()

    val nullable: Boolean
        get() {
            return parameterTag.nullable
        }
}
