package ch.bailu.gtk.converter;

import ch.bailu.gtk.model.ParameterModel;

public class JniDefaultTypeConverter extends JniTypeConverter{

    private final ParameterModel model;
    public JniDefaultTypeConverter(ParameterModel p) {
        model = p;
    }

    @Override
    public String getAllocateResourceString() {
        return "    const "+ model.getGtkType() + " __" + model.getName() + " = (" + model.getGtkType() + ")" + " " + model.getName() + ";\n";
    }

    @Override
    public String getFreeResourcesString() {
        return "";
    }

    @Override
    public String getCallSignatureString() {
        return "__" + model.getName();
    }

    @Override
    public String getJniType() {
        if (model.isVoid()) {
            return "void";
        }

        return "j" + model.getImpType().toLowerCase();
    }

    @Override
    public String getImpDefaultConstant() {
        if ("int".equals(model.getImpType())) {
            return "0";
        }
        if ("long".equals(model.getImpType())) {
            return "0";
        }
        if ("double".equals(model.getImpType())) {
            return "0d";
        }
        if ("float".equals(model.getImpType())) {
            return "0f";
        }
        return "null";
    }

    @Override
    public String getJniSignatureID() {
        if ("int".equals(model.getImpType())) {
            return "I";
        }
        if ("long".equals(model.getImpType())) {
            return "J";
        }
        if ("double".equals(model.getImpType())) {
            return "D";
        }
        if ("float".equals(model.getImpType())) {
            return "F";
        }
        return "V";
    }

    @Override
    public String getJniCallbackMethodName() {
        if ("int".equals(model.getImpType())) {
            return "CallStaticIntMethod";
        }
        if ("long".equals(model.getImpType())) {
            return "CallStaticLongMethod";
        }
        if ("double".equals(model.getImpType())) {
            return "CallStaticDoubleMethod";
        }
        if ("float".equals(model.getImpType())) {
            return "CallStaticFloatMethod";
        }
        return "CallStaticVoidMethod";
    }

}
