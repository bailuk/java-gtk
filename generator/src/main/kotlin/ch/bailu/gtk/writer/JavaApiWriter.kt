package ch.bailu.gtk.writer

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
        a(getJavaDoc(classModel.doc,0))
        a("public class ${classModel.apiName} extends ${classModel.apiParentName} {\n")
    }

    override fun writeInterface(classModel : ClassModel) {
        start()
        a("public interface ${classModel.apiName} {\n")
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
        a(getJavaDoc(methodModel.doc,4))
        a("""
            public ${getStatic(selfCall)} ${methodModel.returnType.apiType} ${methodModel.apiName}(${getSignature(methodModel.getParameters())}) ${getThrowsExtension(methodModel)} {
                ${getCallbackConnections(methodModel)}
                ${getFunctionCall(classModel, methodModel, selfCall)};
            }
            """.replaceIndent(" ".repeat(4)))
    }

    private fun getJavaDoc(doc: String, intent: Int): String {
        if (doc.length > 0) {
            val space = " ".repeat(intent);
            val docOut = doc.replace('\\', '-').replaceIndent("${space} * ")
            return "${space}/**\n${docOut}\n${space}**/\n"
        }
        return ""
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
            getSelfCallSignature(m.getParameters());
        } else {
            getCallSignature(m.getParameters(), "")
        }
    }

    private fun getFunctionCall(c : ClassModel, m : MethodModel, selfCall : Boolean) : String {
        val result = StringBuilder();
        val signature = getCallSignature(m, selfCall);

        if (m.returnType.isVoid) {
            result.append("${c.impName}.${m.apiName}(${signature})")

        } else if (m.returnType.isJavaNative) {
            result.append("return ${c.impName}.${m.apiName}(${signature})")

        } else {
            result.append("return new ${m.returnType.apiType}(${c.impName}.${m.apiName}(${signature}))")
        }
        return result.toString()
    }


    private fun getCallbackConnections (methodModel: MethodModel) : String {
        var result = StringBuilder()
        var del = "";
        if (methodModel.hasCallback()) {

            for (p in methodModel.getParameters()) {
                if (p.isCallback) {
                    result.append("${del}ch.bailu.gtk.Callback.put(0, \"${p.callbackModel?.apiName}\", ${p.name});\n")
                    del = " ".repeat(16)
                }
            }
        }
        return result.toString()
    }


    private fun getThrowsExtension(methodModel : MethodModel) : String {
        return if (methodModel.throwsError) {
            "throws ch.bailu.gtk.exception.AllocationError"
        } else {
            ""
        }
    }


    private fun getThrowsOnNullSatement(classModel: ClassModel, methodModel : MethodModel) : String {
        val msg = "${classModel.apiName}:${methodModel.apiName}"

        return if (methodModel.throwsError) {
            "throw new ch.bailu.gtk.exception.AllocationError(\"${msg}\")"
        } else {
            "throw new NullPointerException(\"${msg}\")"
        }
    }


    override fun writeInternalConstructor(classModel : ClassModel) {
        start(1)

        a("""
            public ${classModel.apiName}(long pointer) {
                super(pointer);
            }
        """.replaceIndent(" ".repeat(4)))

    }


    override fun writeMallocConstructor(classModel : ClassModel) {
        if (classModel.hasDefaultConstructor() == false) {
            start(1)

            a ("""
                
                public ${classModel.apiName}() {
                    super(${classModel.impName}.newFromMalloc());
                }
                
                public void destroy() {
                    ch.bailu.gtk.type.ImpUtil.destroy(getCPointer());
                }
            """.replaceIndent(" ".repeat(4)))

        }
    }

    override fun writeInterfaceMethod(classModel: ClassModel, m: MethodModel) {
        start(1)
        a("        public ${m.returnType.apiType} ${m.apiName}(${getSignature(m.getParameters())});\n")
    }



    override fun writeConstructor(classModel : ClassModel, methodModel : MethodModel) {
        start(1);
        a("""
    public ${classModel.apiName}(${getSignature(methodModel.getParameters())}) {
        super(${classModel.impName}.${methodModel.apiName}(${getFactoryCallSignature(methodModel.getParameters())}));
    }
            
""")
    }

    override fun writeFactory(classModel : ClassModel, methodModel : MethodModel) {
        start(1);

        a("""
    public static ${classModel.apiName} ${methodModel.apiName}${classModel.apiName}(${getSignature(methodModel.getParameters())}) ${getThrowsExtension(methodModel)} {
        long pointerToObject = ${classModel.impName}.${methodModel.apiName}(${getFactoryCallSignature(methodModel.getParameters())});
               
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
        a("    public void ").a(getJavaSignalMethodName(methodModel.name)).a("(").a(getJavaSignalInterfaceName(methodModel.name)).a(" observer) {\n");
        a("        ch.bailu.gtk.Callback.put(getCPointer(), \"").a(methodModel.apiName).a("\", observer);\n");
        a("        ").a(classModel.impName).a(".").a(getJavaSignalMethodName(methodModel.name)).a("(getCPointer());\n");
        a("    }\n");
        a("    public interface ").a(getJavaSignalInterfaceName(methodModel.name)).a(" {\n");
        a("        ").a(methodModel.returnType.apiType).a(" ").a(getJavaSignalMethodName(methodModel.name)); writeSignature(methodModel); a(";\n");
        a("    }\n");
    }


    override fun writeCallback(classModel: ClassModel, methodModel: MethodModel) {
        start(1)

        a("""
   public interface ${getJavaSignalInterfaceName(methodModel.name)} {
      ${methodModel.returnType.apiType} ${getJavaSignalMethodName(methodModel.name)}(${getSignature(methodModel.getParameters())});
   }

""")


    }

    override fun writeField(classModel : ClassModel, parameterModel : ParameterModel) {
        val parameters : MutableList<ParameterModel> = ArrayList()

        val getter = getJavaFieldGetterName(parameterModel.name)
        val setter = getJavaFieldSetterName(parameterModel.name)

        start(1)

        if (parameterModel.isJavaNative) {
            a("""
                
    public ${parameterModel.apiType} ${getter}() {
        return ${classModel.impName}.${getter}(${getSelfCallSignature(parameters)});
    } 
    """)

        } else {
            a("""
                
    public ${parameterModel.apiType} ${getter}() {
        return new ${parameterModel.apiType}(${classModel.impName}.${getter}(${getSelfCallSignature(parameters)}));
    }
    """)

        }

        if (parameterModel.isWriteable && !parameterModel.isDirectType) {
            parameters.add(parameterModel)
            a("""
                
    public void ${setter}(${getSignature(parameters)}) {        
        ${classModel.impName}.${setter}(${getSelfCallSignature(parameters)});
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
            result.append("${del}${p.apiType} ${p.name}")
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
                result.append("${del}${p.name}")
                del = ", "

            } else if (!p.isCallback) {
                result.append("${del}toCPointer(${p.name})")
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

            if (p.isJavaNative) {
                result.append(p.name)
            } else {
                result.append("toCPointer(${p.name})")
            }
            del = ", ";
        }
        return result.toString()
    }
}
