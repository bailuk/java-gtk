package ch.bailu.gtk.writer

import ch.bailu.gtk.converter.JavaNames
import ch.bailu.gtk.model.ClassModel;
import ch.bailu.gtk.model.MethodModel;
import ch.bailu.gtk.model.Model;
import ch.bailu.gtk.model.NameSpaceModel;
import ch.bailu.gtk.model.ParameterModel;

class JavaImpWriter extends CodeWriter {

    JavaImpWriter(Writer writer) {
        super(writer);
    }

    @Override
    void writeStart(ClassModel classModel, NameSpaceModel namespaceModel) throws IOException {
        super.writeStart(classModel, namespaceModel)
        a("\npackage " + namespaceModel.getFullNamespace() + ";\n");
        end(3);
    }

    @Override
    void writeClass(ClassModel classModel) throws IOException {
        start();
        a("class " + classModel.getImpName() + " {\n");
    }

    @Override
    void writeInterface(ClassModel classModel) {}

    @Override
    void writeInternalConstructor(ClassModel classModel) throws IOException {}

    @Override
    void writeUnsupported(Model m) throws IOException {
        start();
        a("    /* Unsupported:" + m.toString() + " */\n");
        end(1);
    }

    @Override
    void writeNativeMethod(ClassModel classModel, MethodModel m) throws IOException {
        start();
        a("    static native ${m.getReturnType().getImpType()} ${m.getApiName()}(${getSelfSignature(m.getParameters())});\n")
        end(1);
    }


    @Override
    void writeMallocConstructor(ClassModel classModel) {
        start()
        a"    static native long newFromMalloc();\n"
        end(1)
    }

    @Override
    public void writeConstructor(ClassModel c, MethodModel m) {}

    @Override
    public void writeFactory(ClassModel c, MethodModel m) {}


    @Override
    public void writePrivateFactory(ClassModel c, MethodModel m) throws IOException {
        start();
        a("    static native long " + m.getApiName());
        writeFactorySignature(m);
        a(";\n");
        end(1);
    }

    @Override
    public void writeConstant(ParameterModel p) {}


    @Override
    public void writeEnd() throws IOException {
        a("}\n");
    }


    @Override
    void writeSignal(ClassModel c, MethodModel m) throws IOException {

        a("""
            static native void ${m.getSignalMethodName()}(long _self);
            static ${m.getReturnType().getImpType()} ${m.getSignalCallbackName()}(${getSelfSignature(m.getParameters())}) {
                String signal = \"${m.getApiName()}\";
                for (java.lang.Object observer : ch.bailu.gtk.Signal.get(_self, signal)) {
                   ${getSignalInterfaceCall(c, m)};
                }
                ${getDefaultReturn(m)}
            }
        """.stripIndent(8))
    }

    private String getDefaultReturn(MethodModel m) {
        if (!m.getReturnType().isVoid()) {
            return "return ${m.getReturnType().getImpDefaultConstant()};"
        }
        return ""
    }

    @Override
    void writeField(ClassModel classModel, ParameterModel p) {
        List<ParameterModel> parameters = new ArrayList<>()

        start();
        a("    static native ${p.getImpType()} ${JavaNames.getGetterName(p.getName())}(${getSelfSignature(parameters)});\n")

        parameters.add(p)
        a("    static native void ${JavaNames.getSetterName(p.getName())}(${getSelfSignature(parameters)});\n")

        end(1);
    }

    @Override
    void writeFunction(ClassModel classModel, MethodModel m) {
        start();
        a("    static native ${m.getReturnType().getImpType()} ${m.getApiName()}(${getSignature(m.getParameters(), '')});\n")
        end(1);

    }

    private String getSignalInterfaceCall(ClassModel c, MethodModel s) throws IOException {
        StringBuilder result = new StringBuilder()

        if (!s.getReturnType().isVoid()) {
            result.append("return ")
        }
        result.append("((${c.getApiName()}.${s.getSignalInterfaceName()})observer).${s.getSignalMethodName()}(${getSignalInterfaceCallSignature(s)})")

        if (!s.getReturnType().isVoid() && !s.getReturnType().isJavaNative()) {
            result.append(".toLong()")
        }
        result
    }

    private String getSignalInterfaceCallSignature(MethodModel s) throws IOException {
        StringBuilder result = new StringBuilder()
        String del = ' '

        for (ParameterModel p: s.getParameters()) {
            result.append(del)
            if (p.isJavaNative()) {
                result.append(p.getName())
            } else {
                result.append("new ${p.getApiType()}(${p.getName()})")
            }
            del = ", ";
        }
        result

    }


    private void writeFactorySignature(MethodModel m) throws IOException {
        a("(");

        String del = " ";


        for (ParameterModel p: m.getParameters()) {
            a(del + p.getImpType() + " " + p.getName());
            del = ", ";
        }
        a(")");
    }

    private String getSelfSignature(List<ParameterModel> parameters) throws IOException {
        "long _self${getSignature(parameters, ', ')}"
    }

    private String getSignature(List<ParameterModel> parameters, String del) throws IOException {
        StringBuilder result = new StringBuilder()

        for (ParameterModel p: parameters) {
            result.append("${del}${p.getImpType()} ${p.getName()}")
            del = ', '
        }
        result
    }

}
