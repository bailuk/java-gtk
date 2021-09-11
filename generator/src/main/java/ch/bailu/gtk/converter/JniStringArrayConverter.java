package ch.bailu.gtk.converter;

import ch.bailu.gtk.model.ClassModel;
import ch.bailu.gtk.model.ParameterModel;

public class JniStringArrayConverter extends JniTypeConverter {

    private final static String PREFIX = "_";
    private final ParameterModel model;

    public JniStringArrayConverter(ParameterModel p) {
        model = p;
    }

    @Override
    public String getAllocateResourceString(ClassModel classModel) {
        return "const " + model.getGtkType() + " " + PREFIX +model.getName() + " = {\"hello\",\"test\"};\n";
    }

    @Override
    public String getFreeResourcesString() {
        return "// free string array\n";
    }

    @Override
    public String getCallSignatureString(ClassModel classModel) {
        return PREFIX + model.getName();
    }

    @Override
    public String getJniType() {
        return "jobjectArray";
    }

    @Override
    public String getImpDefaultConstant() {
        return null;
    }

    @Override
    public String getJniSignatureID() {
        return "Ljava/lang/Object;";
    }

    @Override
    public String getJniCallbackMethodName() {
        return "unknown";
    }

}
