package ch.bailu.gtk.converter.jni

import ch.bailu.gtk.model.StructureModel
import ch.bailu.gtk.model.ParameterModel

class JniPointerConverter(parameter: ParameterModel) : JniTypeConverter() {

    private val model = parameter


    override fun getAllocateResourceString(structureModel: StructureModel): String {
        return ""
    }


    override fun getCallSignatureString(structureModel: StructureModel): String {
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