package ch.bailu.gtk.converter.jni

import ch.bailu.gtk.model.StructureModel
import ch.bailu.gtk.model.ParameterModel

abstract class JniTypeConverter {
    companion object {
        fun factory(parameter: ParameterModel): JniTypeConverter {
            return if (parameter.isCallback) {
                JniCallbackConverter(parameter)
            } else if (parameter.isJavaNative) {
                JniDefaultTypeConverter(parameter)
            } else {
                JniPointerConverter(parameter)
            }
        }
    }

    abstract fun getAllocateResourceString(structureModel: StructureModel): String
    abstract fun getCallSignatureString(structureModel: StructureModel): String

    abstract fun getJniType(): String

    abstract fun getImpDefaultConstant(): String

    abstract fun getJniSignatureID(): String

    abstract fun getJniCallbackMethodName(): String
}