package ch.bailu.gtk.writer

import ch.bailu.gtk.converter.JavaNames
import ch.bailu.gtk.model.*
import java.io.Writer

class JavaImpWriter(writer : Writer) : CodeWriter(writer) {


    override fun writeStart(classModel : ClassModel, namespaceModel : NamespaceModel) {
        super.writeStart(classModel, namespaceModel)
        a("\npackage " + namespaceModel.fullNamespace + ";\n");
        end(3);
    }

    override fun writeClass(classModel : ClassModel) {
        start();
        a("class " + classModel.impName + " {\n");
    }

    override fun writeInterface(classModel : ClassModel) {}

    override fun writeInternalConstructor(classModel : ClassModel) {}

    override fun writeUnsupported(model : Model) {
        start()
        a("    /* Unsupported:${model} */\n")
        end(1)
    }

    override fun writeNativeMethod(classModel : ClassModel, m : MethodModel) {
        start();
        a("    static native ${m.returnType.impType} ${m.apiName}(${getSelfSignature(m.parameters)});\n")
        end(1);
    }


    override fun writeMallocConstructor(classModel : ClassModel) {
        start()
        a("    static native long newFromMalloc();\n")
        end(1)
    }

    override fun writeConstructor(c : ClassModel, m : MethodModel) {}

    override fun writeFactory(c : ClassModel, m : MethodModel) {}


    override fun writePrivateFactory(c : ClassModel, m : MethodModel) {
        start();
        a("    static native long " + m.getApiName());
        writeFactorySignature(m);
        a(";\n");
        end(1);
    }

    override fun writeConstant(p : ParameterModel) {}


    override fun writeEnd() {
        a("}\n");
    }


    override fun writeSignal(c : ClassModel, m : MethodModel) {

        a("""
            
    static native void ${m.signalMethodName}(long _self);
    static ${m.getReturnType().getImpType()} ${m.signalCallbackName}(${getSelfSignature(m.parameters)}) {
        String signal = "${m.apiName}";
        for (java.lang.Object observer : ch.bailu.gtk.Signal.get(_self, signal)) {
            ${getSignalInterfaceCall(c, m)};
        }
        ${getDefaultReturn(m)}
    }
    
        """.trimMargin())
    }

    private fun getDefaultReturn(m : MethodModel) : String {
        if (!m.getReturnType().isVoid()) {
            return "return ${m.getReturnType().getImpDefaultConstant()};"
        }
        return ""
    }

    override fun writeField(classModel : ClassModel, p : ParameterModel) {
        val parameters : MutableList<ParameterModel> = ArrayList()

        start();
        a("static native ${p.getImpType()} ${JavaNames.getGetterName(p.getName())}(${getSelfSignature(parameters)});\n")

        parameters.add(p)
        a("static native void ${JavaNames.getSetterName(p.getName())}(${getSelfSignature(parameters)});\n")

        end(1);
    }

    override
    fun writeFunction(classModel : ClassModel, m : MethodModel) {
        start();
        a("    static native ${m.getReturnType().getImpType()} ${m.getApiName()}(${getSignature(m.getParameters(), "")});\n")
        end(1);

    }

    private fun getSignalInterfaceCall(c : ClassModel, s : MethodModel) : String {
        val result = StringBuilder()

        if (!s.getReturnType().isVoid()) {
            result.append("return ")
        }
        result.append("((${c.getApiName()}.${s.getSignalInterfaceName()})observer).${s.getSignalMethodName()}(${getSignalInterfaceCallSignature(s)})")

        if (!s.returnType.isVoid && !s.returnType.isJavaNative) {
            result.append(".toLong()")
        }
        return result.toString()
    }

    private fun getSignalInterfaceCallSignature(s : MethodModel) : String {
        val result = StringBuilder()
        var del = " "

        for (p in s.getParameters()) {
            result.append(del)
            if (p.isJavaNative()) {
                result.append(p.getName())
            } else {
                result.append("new ${p.apiType}(${p.getName()})")
            }
            del = ", ";
        }
        return result.toString()

    }


    private fun writeFactorySignature(m : MethodModel) {
        a("(");

        var del = " ";


        for (p in m.getParameters()) {
            a(del + p.impType + " " + p.name);
            del = ", ";
        }
        a(")");
    }

    private fun getSelfSignature(parameters : List<ParameterModel>) : String {
        return "long _self${getSignature(parameters, ", ")}"
    }

    private fun getSignature(parameters : List<ParameterModel>, del : String) : String {
        val result = StringBuilder()
        var del = del

        for (p in parameters) {
            result.append("${del}${p.impType} ${p.name}")
            del = ", "
        }
        return result.toString()
    }
}
