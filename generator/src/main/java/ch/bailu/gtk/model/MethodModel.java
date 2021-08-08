package ch.bailu.gtk.model;

import java.util.ArrayList;
import java.util.List;

import ch.bailu.gtk.converter.JavaNames;
import ch.bailu.gtk.tag.MethodTag;
import ch.bailu.gtk.tag.ParameterTag;

public class MethodModel extends Model {

    private String gtkName;
    private String javaName;
    private List<ParameterModel> parameters = new ArrayList<>(5);
    private ParameterModel returnType;

    private MethodModel call;

    private boolean constructorType;
    private boolean throwsError = false;


    // simple method with return type and parameters can be a factory
    public MethodModel(String namespace, MethodTag method) {
        throwsError = method.throwsError();
        gtkName = method.getIdentifier();
        javaName = JavaNames.toJavaMethodName(method.getName());

        returnType = new ParameterModel(namespace, method.getReturnValue());
        setSupported(returnType.isSupported());

        for (ParameterTag t : method.getParameters()) {
            var parameterModel = new ParameterModel(namespace, t);
            parameters.add(parameterModel);
            setSupported(parameterModel.isSupported());
        }

        constructorType = "new".equals(method.getName());
    }

    // constructor with caller
    public MethodModel(String name, MethodModel methodModel) {
        throwsError = methodModel.throwsError();
        gtkName = methodModel.getGtkName();
        call = methodModel;
        parameters = methodModel.parameters;
        javaName = name;
        constructorType = true;
        setSupported(methodModel.isSupported());
    }


    public boolean isConstructorType() {
        return constructorType;
    }

    public ParameterModel getReturnType() {
        return returnType;
    }

    public String getName() {
        return javaName;
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
        .append(getClass().getSimpleName())
        .append(":").append(getReturnType().toString())
        .append(":").append(getName());


        for (ParameterModel p: getParameters()) {
            result.append(":").append(p.toString());
        }
        return result.toString();
    }

    public String getApiName() {
        return javaName;
    }

    public String getGtkName() {
        return gtkName;
    }

    public boolean throwsError() {
        return throwsError;
    }
}
