package ch.bailu.gtk.model

import ch.bailu.gtk.converter.isEnum
import ch.bailu.gtk.converter.jni.JniTypeConverter
import ch.bailu.gtk.converter.jni.JniTypeConverter.Companion.factory
import ch.bailu.gtk.log.colonList
import ch.bailu.gtk.model.filter.filterValues
import ch.bailu.gtk.model.type.CType
import ch.bailu.gtk.model.type.ClassType
import ch.bailu.gtk.model.type.JavaType
import ch.bailu.gtk.parser.tag.ParameterTag
import ch.bailu.gtk.writer.fixToken
import ch.bailu.gtk.writer.getJavaSignalInterfaceName

class ParameterModel(namespace: String, private val parameterTag: ParameterTag, toUpper: Boolean, supportsDirectAccess: Boolean) :
    Model() {

    private val cType: CType
    private val classType: ClassType = ClassType(namespace, parameterTag, supportsDirectAccess)
    private val jType: JavaType

    var name: String = if (toUpper) {
        fixToken(parameterTag.getName().uppercase())
    } else {
        fixToken(parameterTag.getName())
    }
    val jniConverter: JniTypeConverter
    val isWriteable: Boolean
    val callbackModel: MethodModel?


    init {
        if (classType.isClass()) {
            cType = CType("void*")
            jType = JavaType("long")
        } else if (isEnum(namespace, parameterTag)) {
            cType = CType("int")
            jType = JavaType("int")
        } else {
            cType = CType(parameterTag.getType())
            jType = JavaType(parameterTag.getType())
        }
        callbackModel = createCallbackModel(classType, namespace)
        jniConverter = factory(this)

        //setSupported("private", parameter.isPrivate());
        setSupported("value", filterValues(parameterTag.value))
        setSupported("jType", jType.isValid())
        setCallbackSupported()
        isWriteable = parameterTag.isWriteable
    }

    private fun createCallbackModel(classType: ClassType, namespace: String): MethodModel? {
        if (classType.isCallback()) {
            val callbackTag = classType.getCallbackTag()
            if (callbackTag != null) {
                return MethodModel(namespace, callbackTag)
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
            return if (classType.isClass()) {
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
            return !classType.isClass()
        }

    val jniType: String
        get() {
            return jniConverter.getJniType()
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


    val impDefaultConstant: String
        get() {
            return jniConverter.getImpDefaultConstant()
        }

    val jniSignatureID: String
        get() {
            return jniConverter.getJniSignatureID()
        }


    val jniCallbackMethodName: String
        get() {
            return jniConverter.getJniCallbackMethodName()
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
