package ch.bailu.gtk.converter;

import ch.bailu.gtk.model.ClassModel;
import ch.bailu.gtk.model.MethodModel;
import ch.bailu.gtk.model.ParameterModel;

public class JniCallbackConverter extends JniTypeConverter{
    private final ParameterModel parameterModel;
    private final ClassModel classModel;
    private final MethodModel methodModel;

    public JniCallbackConverter(ParameterModel parameterModel, MethodModel methodModel, ClassModel classModel) {
        this.methodModel = methodModel;
        this.parameterModel = parameterModel;
        this.classModel = classModel;
    }

    @Override
    public String getAllocateResourceString() {
        return "";
    }

    @Override
    public String getFreeResourcesString() {
        return "";
    }

    @Override
    public String getCallSignatureString() {
        return "G_CALLBACK (" + classModel.getCSignalCallbackName(methodModel)+ parameterModel.getName()+ ")";
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
