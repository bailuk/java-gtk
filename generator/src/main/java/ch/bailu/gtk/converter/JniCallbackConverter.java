package ch.bailu.gtk.converter;

import ch.bailu.gtk.model.ClassModel;
import ch.bailu.gtk.model.ParameterModel;

import static ch.bailu.gtk.writer.NamesKt.getCCallbackName;

public class JniCallbackConverter extends JniTypeConverter {
    private final ParameterModel parameterModel;

    public JniCallbackConverter(ParameterModel parameterModel) {
        this.parameterModel = parameterModel;
    }

    @Override
    public String getAllocateResourceString(ClassModel classModel) {
        return "    const "+ parameterModel.getGtkType() + " __" + parameterModel.getName() + " = (" + parameterModel.getGtkType() + ")" + " " + getCCallbackName(classModel, parameterModel) + ";\n";
    }

    @Override
    public String getFreeResourcesString() {
        return "";
    }

    @Override
    public String getCallSignatureString(ClassModel classModel) {
        return " __" + parameterModel.getName();
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
