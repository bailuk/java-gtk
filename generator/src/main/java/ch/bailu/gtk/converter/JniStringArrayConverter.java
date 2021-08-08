package ch.bailu.gtk.converter;

import ch.bailu.gtk.model.ParameterModel;

public class JniStringArrayConverter extends JniTypeConverter {

    private final static String PREFIX = "_";
    private final ParameterModel model;

    public JniStringArrayConverter(ParameterModel p) {
        model = p;
    }

    @Override
    public String getAllocateResourceString() {
        return "     const " + model.getGtkType() + " " + PREFIX +model.getName() + " = {\"hello\",\"test\"};\n";
    }

    @Override
    public String getFreeResourcesString() {
        return "// free string array\n";
    }

    @Override
    public String getCallSignatureString() {
        return PREFIX + model.getName();
    }

    @Override
    public String getJniType() {
        return "jobjectArray";
    }
}
