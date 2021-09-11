package ch.bailu.gtk.converter;

import ch.bailu.gtk.model.ClassModel;
import ch.bailu.gtk.model.ParameterModel;

public class JniPointerConverter extends JniTypeConverter{

    private final ParameterModel model;

    public JniPointerConverter(ParameterModel parameter) {
        model = parameter;
    }

    @Override
    public String getAllocateResourceString(ClassModel classModel) {
        return "";
    }

    @Override
    public String getFreeResourcesString() {
        return "";
    }

    @Override
    public String getCallSignatureString(ClassModel classModel) {
        return "(void*) " + model.getName();
    }

    @Override
    public String getJniType() {
        return "jlong";
    }


    @Override
    public String getImpDefaultConstant() {
        return "0";
    }

    @Override
    public String getJniSignatureID() {
        return "J";
    }

    @Override
    public String getJniCallbackMethodName() {
        return "CallStaticLongMethod";
    }
}
