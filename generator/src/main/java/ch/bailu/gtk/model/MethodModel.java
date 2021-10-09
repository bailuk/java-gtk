package ch.bailu.gtk.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ch.bailu.gtk.tag.MethodTag;
import ch.bailu.gtk.tag.ParameterTag;

import static ch.bailu.gtk.writer.NamesKt.getJavaMethodName;
import static ch.bailu.gtk.writer.NamesKt.getJavaSignalName;

public class MethodModel extends Model {

    private final String name;
    private final String gtkName;
    private List<ParameterModel> parameters = new ArrayList<>(5);
    private ParameterModel returnType;

    private MethodModel call;

    private final boolean constructorType;
    private final boolean throwsError;
    private final List<MethodModel> callbackModel = new ArrayList<>();

    private String doc = "";

    // simple method with return type and parameters can be a factory
    public MethodModel(String namespace, MethodTag method) {
        throwsError = method.throwsError();
        gtkName = method.getIdentifier();
        name = method.getName();

        doc = method.getDoc();
        returnType = new ParameterModel(namespace, method.getReturnValue(), false, false);

        setSupported("Deprecated", !method.isDeprecated());
        setSupported("Return value", returnType.isSupported());
        setSupported("Return cb", !returnType.isCallback());

        for (ParameterTag t : method.getParameters()) {
            if (!t.isVarargs()) {
                var parameterModel = new ParameterModel(namespace, t, false, false);
                parameters.add(parameterModel);
                setSupported(parameterModel.getSupportedState(), parameterModel.isSupported());

                if (parameterModel.isCallback()) {
                    callbackModel.add(parameterModel.getCallbackModel());
                }
            }
        }

        constructorType = "new".equals(method.getName());
    }

    public boolean hasCallback() {
        return !callbackModel.isEmpty();
    }

    public List<MethodModel> getCallbackModel() {
        return callbackModel;
    }

    // constructor with caller
    public MethodModel(MethodModel methodModel) {
        throwsError = methodModel.throwsError();
        gtkName = methodModel.getGtkName();
        call = methodModel;
        parameters = methodModel.parameters;
        this.name = methodModel.name;
        constructorType = true;
        setSupported(methodModel.getSupportedState(), methodModel.isSupported());
    }


    public boolean isConstructorType() {
        return constructorType;
    }

    public ParameterModel getReturnType() {
        return returnType;
    }


    public List<ParameterModel> getParameters() {
        return parameters;
    }

    public MethodModel getCall() {
        return call;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder()
        .append(getSupportedState())
        .append(":").append(getReturnType().toString())
        .append(":").append(getApiName());


        for (ParameterModel p: getParameters()) {
            result.append(":").append(p.toString());
        }
        return result.toString();
    }

    public String getApiName() {
        return getJavaMethodName(name);
    }

    public String getGtkName() {
        return gtkName;
    }

    public boolean throwsError() {
        return throwsError;
    }

    public String getSignalMethodName() {
        return getJavaSignalName("on", name);
    }

    public String getSignalInterfaceName() {
        return getJavaSignalName("On", name);
    }

    public String getSignalCallbackName() {
        return getJavaSignalName("callbackOn", name);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodModel that = (MethodModel) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(gtkName, that.gtkName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, gtkName);
    }

    public String getDoc() {
        return doc;
    }
}
