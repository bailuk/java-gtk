package ch.bailu.gtk.converter.jni

import ch.bailu.gtk.model.StructureModel
import ch.bailu.gtk.model.ParameterModel

class JniDefaultTypeConverter(parameterModel: ParameterModel) : JniTypeConverter() {
    private val model = parameterModel

    override fun getAllocateResourceString(structureModel: StructureModel): String {
        return "const ${model.gtkType} __${model.name} = (${model.gtkType}) ${model.name};"
    }

    override fun getCallSignatureString(structureModel: StructureModel): String {
        return "__" + model.name
    }

    override fun getJniType(): String {
        return if (model.isVoid) {
            "void"
        } else "j" + model.impType.lowercase()
    }

    override fun getImpDefaultConstant(): String {
        if ("int" == model.impType) {
            return "0"
        }
        if ("long" == model.impType) {
            return "0"
        }
        if ("double" == model.impType) {
            return "0d"
        }
        return if ("float" == model.impType) {
            "0f"
        } else "null"
    }

    override fun getJniSignatureID(): String {
        if ("int" == model.impType) {
            return "I"
        }
        if ("long" == model.impType) {
            return "J"
        }
        if ("double" == model.impType) {
            return "D"
        }
        return if ("float" == model.impType) {
            "F"
        } else "V"
    }

    override fun getJniCallbackMethodName(): String {
        if ("int" == model.impType) {
            return "CallStaticIntMethod"
        }
        if ("long" == model.impType) {
            return "CallStaticLongMethod"
        }
        if ("double" == model.impType) {
            return "CallStaticDoubleMethod"
        }
        return if ("float" == model.impType) {
            "CallStaticFloatMethod"
        } else "CallStaticVoidMethod"
    }
}