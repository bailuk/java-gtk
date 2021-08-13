package ch.bailu.gtk.writer


import ch.bailu.gtk.model.ClassModel;
import ch.bailu.gtk.model.MethodModel;
import ch.bailu.gtk.model.Model;
import ch.bailu.gtk.model.NameSpaceModel;
import ch.bailu.gtk.model.ParameterModel;

public class JavaApiWriter extends CodeWriter {

    public JavaApiWriter(Writer writer) {
        super(writer);
    }

    @Override
    public void writeStart(ClassModel classModel, NameSpaceModel namespace) throws IOException {
        a("/* this file is auto generated */\n").a("\n\n");
        a("package ").a(namespace.getFullNamespace()).a(";\n");
        end(3);
    }


    @Override
    public void writeClass(ClassModel classModel) throws IOException {
        start();
        a("public class ").a(classModel.getApiName()).a(" extends ").a(classModel.getApiParentName()).a(" {\n");
    }

    @Override
    public void writeInterface(ClassModel classModel) throws IOException {
        start();
        a("public interface " + classModel.getApiName()  + " {\n");
    }

    @Override
    public void writeUnsupported(Model m) throws IOException {
        start(1);
        a("    /* Unsupported:" + m.toString() + " */\n");
    }

    @Override
    public void writeInterfaceMethod(MethodModel m) throws IOException {
        start(1);
        a("    " + m.getReturnType().getApiType() + " " + m.getApiName());
        writeSignature(m);
        a(";\n");
    }


    @Override
    public void writeNativeMethod(ClassModel classModel, MethodModel m) throws IOException {
        start(1);
        a("    public ").a(m.getReturnType().getApiType()).a(" ").a(m.getApiName());
        writeSignature(m);

        a(" {\n        ");


        if (m.getReturnType().isVoid()) {
                a(classModel.getImpName()).
                    a(".").
                    a(m.getApiName());
            writeSelfCallSignature(m);

        } else if (m.getReturnType().isJavaNative()) {
            a("return ").
                    a(classModel.getImpName()).
                    a(".").
                    a(m.getApiName());
            writeSelfCallSignature(m);

        } else {
                a("return new ").
                    a(m.getReturnType().getApiType()).
                    a("(").
                    a(classModel.getImpName()).
                    a(".").
                    a(m.getApiName());
            writeSelfCallSignature(m);
            a(")");
        }

        a(";\n    }\n");
    }

    @Override
    public void writeInternalConstructor(String className) throws IOException {
        start(1);
        a("    public " + className + "(long pointer) { super(pointer);}\n");
    }

    @Override
    public void writeConstructor(ClassModel classModel, MethodModel m) throws IOException {
        start(1);
        a("    public " + classModel.getApiName());
        writeSignature(m);
        a(" {\n");

        m = m.getCall();
        a("        super(").a(classModel.getImpName()).a(".").a(m.getApiName());
        writeFactoryCallSignature(m);
        a(");\n    }\n");
    }

    @Override
    public void writeFactory(ClassModel classModel, MethodModel m) throws IOException {
        start(1);
        a("    public static ").a(classModel.getApiName()).a(" ").a(m.getCall().getApiName() + classModel.getApiName());
        writeSignature(m);
        a(" {\n");

        m = m.getCall();
        a("        return new ").a(classModel.getApiName()).a("(").a(classModel.getImpName()).a(".").a(m.getApiName());
        writeFactoryCallSignature(m);
        a(");\n    }\n");
    }

    @Override
    public void writePrivateFactory(ClassModel c, MethodModel m) {}


    @Override
    public void writeConstant(ParameterModel p) throws IOException {
        start(1);
        a("    " + p.getApiType() + " " + p.getName() + " = " + p.getValue()+ ";\n");
    }

    @Override
    public void writeEnd() throws IOException {
        start();
        a("}\n");
    }



    @Override
    public void writeSignal(ClassModel c, MethodModel m) throws IOException {
        start(1);
        a("    public void ").a(m.getSignalMethodName()).a("(").a(m.getSignalInterfaceName()).a(" observer) {\n");
        a("        ch.bailu.gtk.Signal.put(toLong(), \"").a(m.getApiName()).a("\", observer);\n");
        a("        ").a(c.getImpName()).a(".").a(m.getSignalMethodName()).a("(toLong());\n");
        a("    }\n");
        a("    public interface ").a(m.getSignalInterfaceName()).a(" {\n");
        a("        ").a(m.getReturnType().getApiType()).a(" ").a(m.getSignalMethodName()); writeSignature(m); a(";\n");
        a("    }\n");
    }


    private void writeSignature(MethodModel m) throws IOException {
        a("(");

        String del = "";
        for (ParameterModel p: m.getParameters()) {
            a(del + p.getApiType() + " " + p.getName());
            del = ", ";
        }
        a(")");
    }

    private void writeSelfCallSignature(MethodModel m) throws IOException {
        a("(");

        String del = ",";

        a("toLong()");

        for (ParameterModel p: m.getParameters()) {
            a(del);

            if (p.isJavaNative()) {
                a(p.getName());

            } else {
                a(p.getName()).a(".toLong()");
            }

        }
        a(")");
    }


    private void writeFactoryCallSignature(MethodModel m) throws IOException {
        a("(");

        String del = " ";

        for (ParameterModel p: m.getParameters()) {
            a(del);

            if (p.isJavaNative()) {
                a(p.getName());
            } else {
                a(p.getName()).a(".toLong()");
            }
            del = ", ";
        }
        a(")");
    }


}
