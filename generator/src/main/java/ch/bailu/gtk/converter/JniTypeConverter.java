package ch.bailu.gtk.converter;

import ch.bailu.gtk.model.ParameterModel;

public abstract class JniTypeConverter {
    public static JniTypeConverter factory(ParameterModel parameter) {
        if (parameter.isJavaNative()) {
/*            if (parameter.isArray()) {
                return new JniArrayConverter(parameter);
            } else*/

            if ("String[]".equals(parameter.getImpType())) {
                return new JniStringArrayConverter(parameter);
            } else  if ("String".equals(parameter.getImpType())) {
                return new JniStringConverter(parameter);
            } else {
                return new JniDefaultTypeConverter(parameter);
            }
        } else {
            return new JniPointerConverter(parameter);
        }
    }

    public abstract String getAllocateResourceString();
    public abstract String getFreeResourcesString();
    public abstract String getCallSignatureString();

    public abstract String getJniType();
}
