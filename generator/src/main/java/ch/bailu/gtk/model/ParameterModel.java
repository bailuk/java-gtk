package ch.bailu.gtk.model;

import ch.bailu.gtk.converter.Filter;
import ch.bailu.gtk.converter.JavaNames;
import ch.bailu.gtk.converter.JniTypeConverter;
import ch.bailu.gtk.tag.MethodTag;
import ch.bailu.gtk.tag.ParameterTag;

public class ParameterModel extends Model {
    private final String name;

    private final CType cType;
    private final ClassType classType;
    private final JavaType jType;

    private final String value;

    private JniTypeConverter jniConverter;

    final private boolean isWriteable;

    private MethodModel callbackModel = null;

    public ParameterModel(String namespace, ParameterTag parameter, boolean toUpper) {

        if (toUpper) {
            name = JavaNames.fixToken(parameter.getName().toUpperCase());
         } else {
            name = JavaNames.fixToken(parameter.getName());
        }

        value = parameter.getValue();

        classType = new ClassType(namespace, parameter);
        if (classType.isValid()) {
            cType = new CType("void*");
            jType = new JavaType("long");
        } else {
            cType = new CType(parameter.getType());
            jType = new JavaType(namespace, parameter);
        }

        if (classType.isCallback()) {
            callbackModel = new MethodModel(namespace, classType.getCallbackTag());
        }


        jniConverter = JniTypeConverter.factory(this);

        //setSupported("private", parameter.isPrivate());
        setSupported("value", Filter.values(name, value));
        setSupported("jType", jType.isValid());
        setSupported("callback", isCallbackSupported());

        this.isWriteable = parameter.isWriteable();
    }


    private boolean isCallbackSupported() {
        if (isCallback()) {
            return callbackModel.isSupported() && !callbackModel.hasCallback();
        }
        return true;
    }

    public String getValue() {
        return saveString(value);
    }
    public String getName() {
        return saveString(name);
    }

    public String getApiType() {
        if (isCallback()) {
            return callbackModel.getSignalInterfaceName();
        }

        if (classType.isValid()) {
            return classType.getFullName();
        }

        return jType.getName();
    }

    public String getImpType() {
        return jType.getName();
    }

    public boolean isVoid() {
        return jType.isVoid();
    }

    public boolean isJavaNative() {
        return classType.isValid() == false;
    }

    public String getJniType() {
        return jniConverter.getJniType();
    }



    public String getGtkType() {
        return cType.getName();
    }

    public String getFreeResourcesString() {
        return jniConverter.getFreeResourcesString();
    }

    public String getAllocateResourceString() {
        return jniConverter.getAllocateResourceString();
    }

    public String getCallSignatureString() {
        return jniConverter.getCallSignatureString();
    }

    @Override
    public String toString() {
        String supported="";
        if (isSupported()) {
            supported = "(s)";
        }

        return "[" + supported + getGtkType() + ":" + getApiType() + ":" + getValue() + "]";
    }

    private static String saveString(String in) {
        if (in == null) return "";
        return in;
    }

    public String getImpDefaultConstant() {
       return jniConverter.getImpDefaultConstant();
    }

    public String getJniSignatureID() {
        return jniConverter.getJniSignatureID();
    }


    public String getJniCallbackMethodName() {
        return jniConverter.getJniCallbackMethodName();
    }

    public boolean isWriteable() {
        return isWriteable;
    }

    public boolean isCallback() {
        return classType.isCallback();
    }

    public MethodModel getCallbackModel() {
        return callbackModel;
    }
}
