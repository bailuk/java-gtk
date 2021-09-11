package ch.bailu.gtk.converter;

import ch.bailu.gtk.model.ClassModel;
import ch.bailu.gtk.model.ParameterModel;

public abstract class JniTypeConverter {
    public static JniTypeConverter factory(ParameterModel parameter) {
        if (parameter.isCallback()) {
            return new JniCallbackConverter(parameter);

        } else if (parameter.isJavaNative()) {
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

    public abstract String getAllocateResourceString(ClassModel classModel);
    public abstract String getFreeResourcesString();
    public abstract String getCallSignatureString(ClassModel classModel);

    public abstract String getJniType();

    public abstract String getImpDefaultConstant();

    public abstract String getJniSignatureID();

    public abstract String getJniCallbackMethodName();
}
