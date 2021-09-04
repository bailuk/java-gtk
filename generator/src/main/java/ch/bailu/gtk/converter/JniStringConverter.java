package ch.bailu.gtk.converter;

import ch.bailu.gtk.model.ParameterModel;

public class JniStringConverter extends JniTypeConverter {

    private final static String PREFIX = "_";
    private final ParameterModel model;

    public JniStringConverter(ParameterModel parameter) {
        model = parameter;
    }

    @Override
    public String getAllocateResourceString() {
        return
                model.getGtkType() + " " + PREFIX + model.getName() + " = NULL;\n" +
                "if (" + model.getName() + "!= NULL) {\n" +
                "    " +PREFIX + model.getName() + " = (*_jenv)->GetStringUTFChars(_jenv, " + model.getName() + ", NULL);\n" +
                "}\n";
    }

    @Override
    public String getFreeResourcesString() {
        return "//(*_jenv)->ReleaseStringUTFChars(_jenv, " + model.getName() + ", " + PREFIX + model.getName() + ");\n";
    }

    @Override
    public String getCallSignatureString() {
        return PREFIX + model.getName();
    }

    @Override
    public String getJniType() {
        return "jstring";
    }


    @Override
    public String getImpDefaultConstant() {
        return "\"\"";
    }

    @Override
    public String getJniSignatureID() {
        return "Ljava/lang/String;";
    }

    @Override
    public String getJniCallbackMethodName() {
        return "CallStaticObjectMethod";
    }


}
