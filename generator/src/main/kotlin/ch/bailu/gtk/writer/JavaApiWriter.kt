package ch.bailu.gtk.writer

import ch.bailu.gtk.converter.JavaNames
import ch.bailu.gtk.model.*
import java.io.Writer


class JavaApiWriter(writer : Writer) : CodeWriter(writer) {

    override fun writeStart(classModel : ClassModel, namespace : NamespaceModel) {
        super.writeStart(classModel, namespace)
        start(3)
        a("package ${namespace.getFullNamespace()};")
        end(3)
    }


    override fun writeClass(classModel : ClassModel) {
        start()
        a("public class ${classModel.getApiName()} extends ${classModel.getApiParentName()} {\n")
    }

    override fun writeInterface(classModel : ClassModel) {
        start()
        a("public interface ${classModel.getApiName()} {\n")
    }

    override fun writeUnsupported(m: Model) {
        start (1)
        a ("    /* Unsupported:${m.toString()} */\n")
    }

    override fun writeNativeMethod(classModel : ClassModel, m : MethodModel) {
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




    override fun writeInternalConstructor(c : ClassModel) {
        start(1)
        a("""
            public ${c.getApiName()}(long pointer) {
                super(pointer);
            }
            
            """) //.stripIndent(8))
    }


    override
    fun writeMallocConstructor(classModel : ClassModel) {
        if (classModel.hasDefaultConstructor() == false) {
            start(1)
            a ("""
            public ${classModel.getApiName()}() {
                super(${classModel.getImpName()}.newFromMalloc());
            }
            """) //.stripIndent(8)
        }
    }


    override fun writeConstructor(classModel : ClassModel, methodModel : MethodModel) {
        start(1);
        a("    public " + classModel.getApiName());
        writeSignature(methodModel);
        a(" {\n");

        var m = methodModel.getCall();
        a("        super(").a(classModel.getImpName()).a(".").a(m.getApiName());
        writeFactoryCallSignature(m);
        a(");\n    }\n");
    }

    override fun writeFactory(classModel : ClassModel, m : MethodModel) {
        start(1);
        a("    public static ").a(classModel.getApiName()).a(" ").a(m.getCall().getApiName() + classModel.getApiName());
        writeSignature(m);
        a(" {\n");

        var m = m.getCall();
        a("        return new ").a(classModel.getApiName()).a("(").a(classModel.getImpName()).a(".").a(m.getApiName());
        writeFactoryCallSignature(m);
        a(");\n    }\n");
    }

    override
    fun writePrivateFactory(c : ClassModel, m : MethodModel) {}


    override fun writeConstant(parameterModel : ParameterModel) {
        start(1)

        var value = parameterModel.value


        if (parameterModel.apiType == "String") {
            value = "\"$value\"";
        } else if ("true".equals(value)){
            value = "ch.bailu.gtk.GTK.TRUE"

        } else if ("false".equals(value)){
            value = "ch.bailu.gtk.GTK.FALSE"
        }

    a("    " + parameterModel.apiType + " " + parameterModel.name + " = " + value + ";\n");

    }

    override
    fun writeEnd() {
        start();
        a("}\n");
    }



    override
    fun writeSignal(c : ClassModel, m : MethodModel) {
        start(1);
        a("    public void ").a(m.getSignalMethodName()).a("(").a(m.getSignalInterfaceName()).a(" observer) {\n");
        a("        ch.bailu.gtk.Signal.put(toLong(), \"").a(m.getApiName()).a("\", observer);\n");
        a("        ").a(c.getImpName()).a(".").a(m.getSignalMethodName()).a("(toLong());\n");
        a("    }\n");
        a("    public interface ").a(m.getSignalInterfaceName()).a(" {\n");
        a("        ").a(m.getReturnType().getApiType()).a(" ").a(m.getSignalMethodName()); writeSignature(m); a(";\n");
        a("    }\n");
    }

    override fun writeField(classModel : ClassModel, parameterModel : ParameterModel) {
        val parameters : MutableList<ParameterModel> = ArrayList()

        val getter = JavaNames.getGetterName(parameterModel.getName())
        val setter = JavaNames.getSetterName(parameterModel.getName())

        start(1)

        if (parameterModel.isJavaNative()) {
            a("""
                
    public ${parameterModel.getApiType()} ${getter}() {
        return ${classModel.getImpName()}.${getter}(${getSelfCallSignature(parameters)});
    } 
    """)

        } else {
            a("""
                
    public ${parameterModel.getApiType()} ${getter}() {
        return new ${parameterModel.getApiType()}(${classModel.getImpName()}.${getter}(${getSelfCallSignature(parameters)}));
    }
    """)

        }

        if (parameterModel.isWriteable()) {
            parameters.add(parameterModel)
            a("""
                
    public void ${setter}(${getSignature(parameters)}) {        
        ${classModel.getImpName()}.${setter}(${getSelfCallSignature(parameters)});
    }
    """)

        }


    }

    override
    fun writeFunction(c : ClassModel, m : MethodModel) {
        start(1);
        a("""
    
    public static ${m.getReturnType().getApiType()} ${m.getApiName()}(${getSignature(m.getParameters())}) {
        ${getFunctionCall(c, m)};
    }
        """)


    }


    private fun getFunctionCall(c : ClassModel, m : MethodModel) : String {
        val result = StringBuilder();
        val signature = getCallSignature(m.getParameters(), "")

        if (m.getReturnType().isVoid()) {
            result.append("${c.getImpName()}.${m.getApiName()}(${signature})")

        } else if (m.getReturnType().isJavaNative()) {
            result.append("return ${c.getImpName()}.${m.getApiName()}(${signature})")

        } else {
            result.append("return new ${m.getReturnType().getApiType()}(${c.getImpName()}.${m.getApiName()}(${signature}))")
        }
        return result.toString()
    }

    private fun writeSignature(m : MethodModel) {
        a("(${getSignature(m.getParameters())})")
    }

    private fun getSignature(parameters : List<ParameterModel>) : String{
        val result = StringBuilder()

        var del = ""
        for (p in parameters) {
            result.append("${del}${p.getApiType()} ${p.getName()}")
            del = ", "
        }
        return result.toString()
    }


    private fun writeSelfCallSignature(m : MethodModel) {
        a("(${getSelfCallSignature(m.getParameters())})")
    }


    private fun getSelfCallSignature(parameters : List<ParameterModel>) : String {
        return "toLong()${getCallSignature(parameters, ", ")}"
    }

    private fun getCallSignature(parameters : List<ParameterModel>, del : String) : String{
        val result = StringBuilder()
        var del = del

        for (p in parameters) {
            if (p.isJavaNative()) {
                result.append("${del}${p.getName()}")
            } else {
                result.append("${del}${p.getName()}.toLong()")
            }
            del = ", "
        }
        return result.toString()
    }


    private fun writeFactoryCallSignature(m : MethodModel) {
        a("(");

        var del = " ";

        for (p in m.getParameters()) {
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
