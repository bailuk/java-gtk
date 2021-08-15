package ch.bailu.gtk.writer

import ch.bailu.gtk.converter.JavaNames
import ch.bailu.gtk.model.*

class JavaApiWriter extends CodeWriter {

    JavaApiWriter(Writer writer) {
        super(writer);
    }

    @Override
    void writeStart(ClassModel classModel, NameSpaceModel namespace) throws IOException {
        super.writeStart(classModel, namespace)
        a"""
            
            package ${namespace.getFullNamespace()};
        """.stripIndent()
        end(3)
    }


    @Override
    void writeClass(ClassModel classModel) throws IOException {
        start()
        a "public class ${classModel.getApiName()} extends ${classModel.getApiParentName()} {\n"
    }

    @Override
    void writeInterface(ClassModel classModel) throws IOException {
        start()
        a"public interface ${classModel.getApiName()} {\n"
    }

    @Override
    void writeUnsupported(Model m) throws IOException {
        start (1)
        a("    /* Unsupported:" + m.toString() + " */\n");
    }

    @Override
    void writeNativeMethod(ClassModel classModel, MethodModel m) throws IOException {
        start(1);
        a("    public ${m.getReturnType().getApiType()} ${m.getApiName()}")
        writeSignature(m)

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
    void writeInternalConstructor(ClassModel c) throws IOException {
        start(1);
        a("""
            public ${c.getApiName()}(long pointer) {
                super(pointer);
            }
            
            """.stripIndent(8))

        if (c.isRecord() && c.hasDefaultConstructor() == false) {
            a("""
            public ${c.getApiName()}() {
                super(${c.getImpName()}.newFromMalloc());
            }
            """.stripIndent(8))
        }
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
    void writeFactory(ClassModel classModel, MethodModel m) throws IOException {
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
    void writePrivateFactory(ClassModel c, MethodModel m) {}


    @Override
    void writeConstant(ParameterModel p) throws IOException {
        start(1);
        a("    " + p.getApiType() + " " + p.getName() + " = " + p.getValue()+ ";\n");
    }

    @Override
    void writeEnd() throws IOException {
        start();
        a("}\n");
    }



    @Override
    void writeSignal(ClassModel c, MethodModel m) throws IOException {
        start(1);
        a("    public void ").a(m.getSignalMethodName()).a("(").a(m.getSignalInterfaceName()).a(" observer) {\n");
        a("        ch.bailu.gtk.Signal.put(toLong(), \"").a(m.getApiName()).a("\", observer);\n");
        a("        ").a(c.getImpName()).a(".").a(m.getSignalMethodName()).a("(toLong());\n");
        a("    }\n");
        a("    public interface ").a(m.getSignalInterfaceName()).a(" {\n");
        a("        ").a(m.getReturnType().getApiType()).a(" ").a(m.getSignalMethodName()); writeSignature(m); a(";\n");
        a("    }\n");
    }

    @Override
    void writeField(ClassModel classModel, ParameterModel p) {
        List<ParameterModel> parameters = new ArrayList()

        String getter = JavaNames.getGetterName(p.getName())
        String setter = JavaNames.getSetterName(p.getName())

        start(1)

        if (p.isJavaNative()) {
            a("""
                public ${p.getApiType()} ${getter}() {
                    return ${classModel.getImpName()}.${getter}(${getSelfCallSignature(parameters)});
                }
                """.stripIndent(12))
        } else {
            a("""
                public ${p.getApiType()} ${getter}() {
                    return new ${p.getApiType()}(${classModel.getImpName()}.${getter}(${getSelfCallSignature(parameters)}));
                }
                """.stripIndent(12))
        }

        if (p.isWriteable()) {
            parameters.add(p)
            a("""
                public void ${setter}(${getSignature(parameters)}) {        
                    ${classModel.getImpName()}.${setter}(${getSelfCallSignature(parameters)});
                }
            """.stripIndent(12))
        }


    }

    @Override
    void writeFunction(ClassModel c, MethodModel m) {
        start(1);
        a("""
            public static ${m.getReturnType().getApiType()} ${m.getApiName()}(${getSignature(m.getParameters())}) {
                ${getFunctionCall(c, m)};
            }

        """.stripIndent(8))
    }


    private String getFunctionCall(ClassModel c, MethodModel m) {
        StringBuilder result = new StringBuilder();
        String signature = getCallSignature(m.getParameters(), '')

        if (m.getReturnType().isVoid()) {
            result.append("${c.getImpName()}.${m.getApiName()}(${signature})")

        } else if (m.getReturnType().isJavaNative()) {
            result.append("return ${c.getImpName()}.${m.getApiName()}(${signature})")

        } else {
            result.append("return new ${m.getReturnType().getApiType()}(${c.getImpName()}.${m.getApiName()}(${signature}))")
        }
        result
    }

    private void writeSignature(MethodModel m) throws IOException {
        a("(${getSignature(m.getParameters())})")
    }

    private String getSignature(List<ParameterModel> parameters) throws IOException {
        StringBuilder result = new StringBuilder()

        String del = ''
        for (ParameterModel p: parameters) {
            result.append("${del}${p.getApiType()} ${p.getName()}")
            del = ', '
        }
        result
    }


    private void writeSelfCallSignature(MethodModel m) throws IOException {
        a("(${getSelfCallSignature(m.getParameters())})")
    }


    private String getSelfCallSignature(List<ParameterModel> parameters) throws IOException {
        "toLong()${getCallSignature(parameters, ', ')}"
    }

    private String getCallSignature(List<ParameterModel> parameters, String del) throws IOException {
        StringBuilder result = new StringBuilder()

        for (ParameterModel p: parameters) {
            if (p.isJavaNative()) {
                result.append("${del}${p.getName()}")
            } else {
                result.append("${del}${p.getName()}.toLong()")
            }
            del = ', '
        }
        result
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
