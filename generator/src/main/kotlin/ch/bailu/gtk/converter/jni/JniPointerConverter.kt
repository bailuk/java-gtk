package ch.bailu.gtk.converter.jni

import ch.bailu.gtk.model.ClassModel
import ch.bailu.gtk.model.ParameterModel

class JniPointerConverter(parameter: ParameterModel) : JniTypeConverter() {

    private val model = parameter


    override fun getAllocateResourceString(classModel: ClassModel): String {
        return ""
    }

    override fun getFreeResourcesString(): String {
        return ""
    }

    override fun getCallSignatureString(classModel: ClassModel): String {
        return "(void*) ${model.name}"
    }

    override fun getJniType(): String {
        return "jlong"
    }


    override fun getImpDefaultConstant(): String {
        return "0"
    }

    override fun getJniSignatureID(): String {
        return "J"
    }

    override fun getJniCallbackMethodName(): String {
        return "CallStaticLongMethod"
    }
}