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

    override fun writeNativeMethod(classModel : ClassModel, methodModel : MethodModel) {
        start();
        a("    static native ${methodModel.returnType.impType} ${methodModel.apiName}(${getSelfSignature(methodModel.parameters)});\n")
        end(1);
    }


    override fun writeMallocConstructor(classModel : ClassModel) {
        start()
        a("    static native long newFromMalloc();\n")
        end(1)
    }

    override fun writeInterfaceMethod(classModel: ClassModel, m: MethodModel) {}

    override fun writeConstructor(classModel : ClassModel, methodModel : MethodModel) {}

    override fun writeFactory(classModel : ClassModel, methodModel : MethodModel) {}


    override fun writePrivateFactory(classModel : ClassModel, methodModel : MethodModel) {
        start();
        a("    static native long " + methodModel.getApiName());
        writeFactorySignature(methodModel);
        a(";\n");
        end(1);
    }

    override fun writeConstant(parameterModel : ParameterModel) {}


    override fun writeEnd() {
        a("}\n");
    }


    override fun writeSignal(classModel : ClassModel, methodModel : MethodModel) {

        a("""
            
    static native void ${methodModel.signalMethodName}(long _self);
    static ${methodModel.getReturnType().getImpType()} ${methodModel.signalCallbackName}(${getSelfSignature(methodModel.parameters)}) {
        String signal = "${methodModel.apiName}";
        for (java.lang.Object observer : ch.bailu.gtk.Signal.get(_self, signal)) {
            ${getSignalInterfaceCall(classModel, methodModel)};
        }
        ${getDefaultReturn(methodModel)}
    }
    
        """.trimMargin())
    }

    override fun writeCallback(classModel: ClassModel, methodModel: MethodModel) {
        a("""
            
    static ${methodModel.getReturnType().getImpType()} ${methodModel.signalCallbackName}(${getSignature(methodModel.parameters, "")}) {
        String signal = "${methodModel.apiName}";
        for (java.lang.Object observer : ch.bailu.gtk.Signal.get(0, signal)) {
            ${getSignalInterfaceCall(classModel, methodModel)};
        }
        ${getDefaultReturn(methodModel)}
    }
    
        """.trimMargin())

    }

    private fun getDefaultReturn(methodModel : MethodModel) : String {
        if (!methodModel.getReturnType().isVoid()) {
            return "return ${methodModel.getReturnType().getImpDefaultConstant()};"
        }
        return ""
    }

    override fun writeField(classModel : ClassModel, parameterModel : ParameterModel) {
        val parameters : MutableList<ParameterModel> = ArrayList()

        start();
        a("static native ${parameterModel.getImpType()} ${JavaNames.getGetterName(parameterModel.getName())}(${getSelfSignature(parameters)});\n")

        parameters.add(parameterModel)
        a("static native void ${JavaNames.getSetterName(parameterModel.getName())}(${getSelfSignature(parameters)});\n")

        end(1);
    }

    override
    fun writeFunction(classModel : ClassModel, methodModel : MethodModel) {
        start();
        a("    static native ${methodModel.getReturnType().getImpType()} ${methodModel.getApiName()}(${getSignature(methodModel.getParameters(), "")});\n")
        end(1);

    }

    private fun getSignalInterfaceCall(classModel : ClassModel, methodModel : MethodModel) : String {
        val result = StringBuilder()

        if (!methodModel.getReturnType().isVoid()) {
            result.append("return ")
        }
        result.append("((${classModel.getApiName()}.${methodModel.getSignalInterfaceName()})observer).${methodModel.getSignalMethodName()}(${getSignalInterfaceCallSignature(methodModel)})")

        if (!methodModel.returnType.isVoid && !methodModel.returnType.isJavaNative) {
            result.append(".toLong()")
        }
        return result.toString()
    }

    private fun getSignalInterfaceCallSignature(methodModel : MethodModel) : String {
        val result = StringBuilder()
        var del = " "

        for (p in methodModel.getParameters()) {
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


    private fun writeFactorySignature(methodModel : MethodModel) {
        a("(");

        var del = " ";


        for (p in methodModel.getParameters()) {
            a(del + p.impType + " " + p.name);
            del = ", ";
        }
        a(")");
    }

    private fun getSelfSignature(parameters : List<ParameterModel>) : String {
        return "long _self${getSignature(parameters, ", ")}"
    }

    private fun getSignature(parameters : List<ParameterModel>, firstDel : String) : String {
        val result = StringBuilder()
        var del = firstDel

        for (p in parameters) {
            if (!p.isCallback) {
                result.append("${del}${p.impType} ${p.name}")
                del = ", "
            }
        }
        return result.toString()
    }
}
