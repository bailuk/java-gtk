package ch.bailu.gtk.writer

import ch.bailu.gtk.converter.JavaNames
import ch.bailu.gtk.model.*
import java.io.Writer


class JavaApiWriter(writer : Writer) : CodeWriter(writer) {

    override fun writeStart(classModel : ClassModel, namespaceModel : NamespaceModel) {
        super.writeStart(classModel, namespaceModel)
        start(3)
        a("package ${namespaceModel.getFullNamespace()};")
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

    override fun writeUnsupported(model: Model) {
        start (1)
        a ("    /* Unsupported:${model} */\n")
    }


    override fun writeNativeMethod(classModel : ClassModel, methodModel : MethodModel) {
        start(2)
        writeFunctionCall(classModel, methodModel, true)
        next()
    }


    private fun writeFunctionCall(classModel : ClassModel, methodModel : MethodModel, selfCall: Boolean) {
        a("""
            public ${getStatic(selfCall)} ${methodModel.getReturnType().apiType} ${methodModel.apiName}(${getSignature(methodModel.parameters)}) ${getThrowsExtension(methodModel)} {
                ${getCallbackConnections(methodModel)}
                ${getFunctionCall(classModel, methodModel, selfCall)};
            }
            """.replaceIndent(" ".repeat(4)))
    }

    private fun getStatic(selfCall : Boolean) : String {
        return if (selfCall) {
            ""
        } else {
            "static"
        }
    }
    override fun writeFunction(classModel : ClassModel, methodModel : MethodModel) {
        start(2)
        writeFunctionCall(classModel, methodModel, false)
        next()
    }

    private fun getCallSignature(m : MethodModel, selfCall : Boolean) : String {
        return if (selfCall) {
            getSelfCallSignature(m.parameters);
        } else {
            getCallSignature(m.getParameters(), "")
        }
    }

    private fun getFunctionCall(c : ClassModel, m : MethodModel, selfCall : Boolean) : String {
        val result = StringBuilder();
        val signature = getCallSignature(m, selfCall);

        if (m.getReturnType().isVoid()) {
            result.append("${c.getImpName()}.${m.getApiName()}(${signature})")

        } else if (m.getReturnType().isJavaNative()) {
            result.append("return ${c.getImpName()}.${m.getApiName()}(${signature})")

        } else {
            result.append("return new ${m.getReturnType().getApiType()}(${c.getImpName()}.${m.getApiName()}(${signature}))")
        }
        return result.toString()
    }


    private fun getCallbackConnections (methodModel: MethodModel) : String {
        var result = StringBuilder()
        var del = "";
        if (methodModel.hasCallback()) {

            for (p in methodModel.parameters) {
                if (p.isCallback) {
                    result.append("${del}ch.bailu.gtk.Callback.put(0, \"${p.callbackModel.apiName}\", ${p.name});\n")
                    del = " ".repeat(16)
                }
            }
        }
        return result.toString()
    }


    private fun getThrowsExtension(methodModel : MethodModel) : String {
        return if (methodModel.throwsError()) {
            "throws ch.bailu.gtk.exception.AllocationError"
        } else {
            ""
        }
    }


    private fun getThrowsOnNullSatement(classModel: ClassModel, methodModel : MethodModel) : String {
        val msg = "${classModel.apiName}:${methodModel.apiName}"

        return if (methodModel.throwsError()) {
            "throw new ch.bailu.gtk.exception.AllocationError(\"${msg}\")"
        } else {
            "throw new NullPointerException(\"${msg}\")"
        }
    }


    override fun writeInternalConstructor(classModel : ClassModel) {
        start(1)

        a("""
            public ${classModel.getApiName()}(long pointer) {
                super(pointer);
            }
        """.replaceIndent(" ".repeat(4)))

    }


    override fun writeMallocConstructor(classModel : ClassModel) {
        if (classModel.hasDefaultConstructor() == false) {
            start(1)

            a ("""
                
                public ${classModel.getApiName()}() {
                    super(${classModel.getImpName()}.newFromMalloc());
                }
                
                public void destroy() {
                    ch.bailu.gtk.type.ImpUtil.destroy(getCPointer());
                }
            """.replaceIndent(" ".repeat(4)))

        }
    }

    override fun writeInterfaceMethod(classModel: ClassModel, m: MethodModel) {
        start(1)
        a("        public ${m.returnType.apiType} ${m.apiName}(${getSignature(m.parameters)});\n")
    }



    override fun writeConstructor(classModel : ClassModel, methodModel : MethodModel) {
        start(1);
        a("""
    public ${classModel.getApiName()}(${getSignature(methodModel.parameters)}) {
        super(${classModel.impName}.${methodModel.apiName}(${getFactoryCallSignature(methodModel.parameters)}));
    }
            
""")
    }

    override fun writeFactory(classModel : ClassModel, methodModel : MethodModel) {
        start(1);

        a("""
    public static ${classModel.apiName} ${methodModel.call.apiName}${classModel.apiName}(${getSignature(methodModel.parameters)}) ${getThrowsExtension(methodModel)} {
        long pointerToObject = ${classModel.impName}.${methodModel.apiName}(${getFactoryCallSignature(methodModel.parameters)});
               
        if (pointerToObject == 0) {
            ${getThrowsOnNullSatement(classModel, methodModel)};
        }
        
        return new ${classModel.apiName}(pointerToObject);
         
    }        

""")

    }

    override
    fun writePrivateFactory(classModel : ClassModel, methodModel : MethodModel) {}


    override fun writeConstant(parameterModel : ParameterModel) {
        start(1)

        var value = parameterModel.value
        var type  = parameterModel.apiType

        if (parameterModel.apiType.endsWith("Str")) {
            type = "String"
            value = "\"$value\"";
        } else if ("true".equals(value)){
            value = "ch.bailu.gtk.GTK.TRUE"

        } else if ("false".equals(value)){
            value = "ch.bailu.gtk.GTK.FALSE"
        }

        a("    " + type + " " + parameterModel.name + " = " + value + ";\n");

    }

    override fun writeEnd() {
        start();
        a("}\n");
    }

    override  fun writeSignal(classModel : ClassModel, methodModel : MethodModel) {
        start(1);
        a("    public void ").a(methodModel.getSignalMethodName()).a("(").a(methodModel.getSignalInterfaceName()).a(" observer) {\n");
        a("        ch.bailu.gtk.Callback.put(getCPointer(), \"").a(methodModel.getApiName()).a("\", observer);\n");
        a("        ").a(classModel.getImpName()).a(".").a(methodModel.getSignalMethodName()).a("(getCPointer());\n");
        a("    }\n");
        a("    public interface ").a(methodModel.getSignalInterfaceName()).a(" {\n");
        a("        ").a(methodModel.getReturnType().getApiType()).a(" ").a(methodModel.getSignalMethodName()); writeSignature(methodModel); a(";\n");
        a("    }\n");
    }


    override fun writeCallback(classModel: ClassModel, methodModel: MethodModel) {
        start(1)

        a("""
   public interface ${methodModel.signalInterfaceName} {
      ${methodModel.returnType.apiType} ${methodModel.signalMethodName}(${getSignature(methodModel.parameters)});
   }

""")


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

        if (parameterModel.isWriteable() && !parameterModel.isDirectType()) {
            parameters.add(parameterModel)
            a("""
                
    public void ${setter}(${getSignature(parameters)}) {        
        ${classModel.getImpName()}.${setter}(${getSelfCallSignature(parameters)});
    }
    """)

        }


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


    private fun getSelfCallSignature(parameters : List<ParameterModel>) : String {
        return "getCPointer()${getCallSignature(parameters, ", ")}"
    }

    private fun getCallSignature(parameters : List<ParameterModel>, firstDel : String) : String{
        val result = StringBuilder()
        var del = firstDel

        for (p in parameters) {
            if (p.isJavaNative) {
                result.append("${del}${p.getName()}")
                del = ", "

            } else if (!p.isCallback) {
                result.append("${del}toCPointer(${p.getName()})")
                del = ", "
            }

        }
        return result.toString()
    }


    private fun getFactoryCallSignature(parameters : List<ParameterModel>) : String {
        val result = StringBuilder()
        var del = " ";

        for (p in parameters) {
            result.append(del)

            if (p.isJavaNative()) {
                result.append(p.getName())
            } else {
                result.append("toCPointer(${p.getName()})")
            }
            del = ", ";
        }
        return result.toString()
    }
}
