package ch.bailu.gtk.writer

import ch.bailu.gtk.model.NamespaceModel
import ch.bailu.gtk.model.*
import java.io.Writer

class JavaImpWriter(writer : Writer) : CodeWriter(writer) {


    override fun writeStart(classModel : ClassModel, namespaceModel : NamespaceModel) {
        super.writeStart(classModel, namespaceModel)
        a("\npackage " + namespaceModel.getFullNamespace() + ";\n");
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
        a("    static native ${methodModel.returnType.impType} ${methodModel.apiName}(${getSelfSignature(methodModel.getParameters())});\n")
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
        a("    static native long " + methodModel.apiName);
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
            
    static native void ${getJavaSignalMethodName(methodModel.name)}(long _self);
    static ${methodModel.returnType.impType} ${getImpJavaSignalCallbackName(methodModel.name)}(${getSelfSignature(methodModel.getParameters())}) {
        String signal = "${methodModel.apiName}";
        for (java.lang.Object observer : ch.bailu.gtk.Callback.get(_self, signal)) {
            ${getSignalInterfaceCall(classModel, methodModel)};
        }
        ${getDefaultReturn(methodModel)}
    }
    
        """.trimMargin())
    }

    override fun writeCallback(classModel: ClassModel, methodModel: MethodModel) {
        a("""
            
    static ${methodModel.returnType.impType} ${getImpJavaSignalCallbackName(methodModel.name)}(${getSignature(methodModel.getParameters(), "")}) {
        String signal = "${methodModel.apiName}";
        for (java.lang.Object observer : ch.bailu.gtk.Callback.get(0, signal)) {
            ${getSignalInterfaceCall(classModel, methodModel)};
        }
        ${getDefaultReturn(methodModel)}
    }
    
        """.trimMargin())

    }

    private fun getDefaultReturn(methodModel : MethodModel) : String {
        if (!methodModel.returnType.isVoid) {
            return "return ${methodModel.returnType.impDefaultConstant};"
        }
        return ""
    }

    override fun writeField(classModel : ClassModel, parameterModel : ParameterModel) {
        val parameters : MutableList<ParameterModel> = ArrayList()

        start();
        a("static native ${parameterModel.impType} ${getJavaFieldGetterName(parameterModel.name)}(${getSelfSignature(parameters)});\n")

        if (parameterModel.isWriteable && !parameterModel.isDirectType) {
            parameters.add(parameterModel)
            a("static native void ${getJavaFieldSetterName(parameterModel.name)}(${getSelfSignature(parameters)});\n")
        }

        end(1);
    }

    override
    fun writeFunction(classModel : ClassModel, methodModel : MethodModel) {
        start();
        a("    static native ${methodModel.returnType.impType} ${methodModel.apiName}(${getSignature(methodModel.getParameters(), "")});\n")
        end(1);

    }

    private fun getSignalInterfaceCall(classModel : ClassModel, methodModel : MethodModel) : String {
        val result = StringBuilder()

        if (!methodModel.returnType.isVoid) {
            result.append("return ")
        }
        result.append("((${classModel.apiName}.${getJavaSignalInterfaceName(methodModel.name)})observer).${getJavaSignalMethodName(methodModel.name)}(${getSignalInterfaceCallSignature(methodModel)})")

        if (!methodModel.returnType.isVoid && !methodModel.returnType.isJavaNative) {
            result.append(".getCPointer()")
        }
        return result.toString()
    }

    private fun getSignalInterfaceCallSignature(methodModel : MethodModel) : String {
        val result = StringBuilder()
        var del = " "

        for (p in methodModel.getParameters()) {
            result.append(del)
            if (p.isJavaNative) {
                result.append(p.name)
            } else {
                result.append("new ${p.apiType}(${p.name})")
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
