package ch.bailu.gtk.converter

import ch.bailu.gtk.model.ClassModel
import ch.bailu.gtk.model.ParameterModel

/*
class JniIntegerConverter(model: ParameterModel) : JniTypeConverter() {

    val model = model;

    override fun getAllocateResourceString(classModel: ClassModel?): String {
        val clsType = "_${model.name}TypeClass"
        val cls = "_${model.name}Class"
        val get = "_${model.name}Getter"
        val getID = "_${model.name}GetID"
        val jget = "\"getValue\""
        val jClassSig = "\"java/lang/Integer\""
        val jgetSig = "\"()Ljava/lang/Integer;\""
        val jobj = "_${model.name}Object"

        return """
            const jclass ${cls} = (*_jenv)->GetObjectClass(_jenv, ${model.name};
            const jmethodID ${get} = (*_jenv)->GetMethodID(_jenv, ${cls}, ${jget}, ${jgetSig});
            const jobject ${jobj} = (*_jenv)->CallObjectMethod(_jenv, ${model.name}, ${get});
            const jclass ${clsType} = (*_jenv)->FindClass(_jenv, ${jClassSig});
            const jmethodID ${getID} = (*_jenv)->GetMethodID(_jenv, ${clsType}, "intValue", "()I");
            const int __${model.name} = (*_jenv)->CallIntMethod(_jenv, ${jobj}, ${getID});
            
        """.trimIndent()

    }

    override fun getFreeResourcesString(): String {
        TODO("Not yet implemented")
    }

    override fun getCallSignatureString(classModel: ClassModel?): String {
        return "(${model.gtkType}) &__${model.name}"
    }

    override fun getJniType(): String {
        return "jobject"
    }

    override fun getImpDefaultConstant(): String {
        return "NULL"
    }

    override fun getJniSignatureID(): String {

    }

    override fun getJniCallbackMethodName(): String {
        TODO("Not yet implemented")
    }

}

 */