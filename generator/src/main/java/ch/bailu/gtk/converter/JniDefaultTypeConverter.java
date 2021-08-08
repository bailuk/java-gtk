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
}
