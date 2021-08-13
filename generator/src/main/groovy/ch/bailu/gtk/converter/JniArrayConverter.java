package ch.bailu.gtk.converter;

import ch.bailu.gtk.model.ParameterModel;

public class JniArrayConverter extends JniTypeConverter{

    private final ParameterModel model;

    public JniArrayConverter(ParameterModel p) {
        model = p;
    }

    @Override
    public String getAllocateResourceString() {
        return "// to array\n";
    }

    @Override
    public String getFreeResourcesString() {
        return "// free array\n";
    }

    @Override
    public String getCallSignatureString() {
        return model.getName();
    }

    @Override
    public String getJniType() {
        return "jobjectArray";
    }

    @Override
    public String getImpDefaultConstant() {
        return "null";
    }

    @Override
    public String getJniSignatureID() {
        return "Ljava/lang/Object";
    }

    @Override
    public String getJniCallbackMethodName() {
        return "unknown";
    }
}
