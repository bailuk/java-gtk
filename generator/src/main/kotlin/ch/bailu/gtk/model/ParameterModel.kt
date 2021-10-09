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

class ParameterModel : Model {
    private val cType: CType
    private val classType: ClassType
    private val jType: JavaType
    private val parameterTag: ParameterTag

    var name: String
    val jniConverter: JniTypeConverter
    val isWriteable: Boolean
    val callbackModel: MethodModel?



    constructor(namespace: String, parameterTag: ParameterTag, toUpper: Boolean, supportsDirectAccess: Boolean) {
        this.parameterTag = parameterTag
        name = if (toUpper) {
            fixToken(parameterTag.getName().uppercase())
        } else {
            fixToken(parameterTag.getName())
        }
        classType = ClassType(namespace, parameterTag, supportsDirectAccess)
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
        setSupported("value", filterValues(parameterTag.getValue()))
        setSupported("jType", jType.isValid())
        setSupported("callback", isCallbackSupported)
        isWriteable = parameterTag.isWriteable()
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


    private val isCallbackSupported: Boolean
        get() {
            return if (isCallback && callbackModel != null) {
                callbackModel.isSupported && !callbackModel.hasCallback()
            } else true
        }


    val apiType: String
        get() {
            if (isCallback && callbackModel != null) {
                return getJavaSignalInterfaceName(callbackModel.name)
            }
            return if (classType.isClass()) {
                classType.getFullName()
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
                parameterTag.getValue(),
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
            return parameterTag.getValue()
        }

}