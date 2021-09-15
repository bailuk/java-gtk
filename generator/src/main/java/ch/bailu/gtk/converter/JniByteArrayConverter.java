package ch.bailu.gtk.converter;


import ch.bailu.gtk.model.ClassModel;
import ch.bailu.gtk.model.ParameterModel;

public class JniByteArrayConverter extends JniTypeConverter{
    private final static String PREFIX = "__";

    private final ParameterModel model;
    final String var;

    public JniByteArrayConverter(ParameterModel parameter) {
        model = parameter;
        var = PREFIX + model.getName();
    }


    @Override
    public String getAllocateResourceString(ClassModel classModel) {

        return  "\n" +
                "    " + model.getGtkType() + " " + var + " = NULL;\n" +
                "    if (" + var + "!= NULL) {\n" +
                "         " + var + " = (*_jenv)->GetByteArrayElements(_jenv, " + model.getName() + ", NULL);\n" +
                "    }\n";
    }

    @Override
    public String getFreeResourcesString() {
        return "    // (*_jenv)->ReleaseByteArrayElements(_jenv, " + model.getName() + ", bufferPtr, 0);";
    }

    @Override
    public String getCallSignatureString(ClassModel classModel) {
        return var;
    }

    @Override
    public String getJniType() {
        return "jbyteArray";
    }

    @Override
    public String getImpDefaultConstant() {
        return "0";
    }

    @Override
    public String getJniSignatureID() {
        return "[Ljava/lang/byte;";
    }

    @Override
    public String getJniCallbackMethodName() {
        return "CallStaticObjectMethod";
    }
}
