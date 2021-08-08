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
        return "    const "+ model.getGtkType() + " "+ PREFIX + model.getName() + " = (*_jenv)->GetStringUTFChars(_jenv, " + model.getName() + ", NULL);\n";
    }

    @Override
    public String getFreeResourcesString() {
        return "    (*_jenv)->ReleaseStringUTFChars(_jenv, " + model.getName() + ", " + PREFIX + model.getName() + ");\n";
    }

    @Override
    public String getCallSignatureString() {
        return PREFIX + model.getName();
    }

    @Override
    public String getJniType() {
        return "jstring";
    }
}
