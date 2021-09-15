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
        a ("    /* Unsupported:${model.toString()} */\n")
    }

    override fun writeNativeMethod(classModel : ClassModel, methodModel : MethodModel) {
        val impCall = "${classModel.getImpName()}.${methodModel.getApiName()}(${getSelfCallSignature(methodModel.parameters)})"

        start(1);
        a("""
    public ${methodModel.getReturnType().getApiType()} ${methodModel.getApiName()}(${getSignature(methodModel.parameters)}) ${getThrowsExtension(methodModel)} {             
""")

        writeCallbackConnections(methodModel)

        if (methodModel.getReturnType().isVoid()) {
            a("        ${impCall};\n")

        } else if (methodModel.getReturnType().isJavaNative()) {
            a("        return ${impCall};\n")

        } else {
            a("""
        long pointerToObject = ${impCall};
        
        if (pointerToObject == 0) {
            ${getThrowsOnNullSatement(classModel, methodModel)};
        }
        
        return new ${methodModel.getReturnType().getApiType()}(pointerToObject);
""")
        }



        a("    }\n\n")
    }


    private fun writeCallbackConnections (methodModel: MethodModel) {
        if (methodModel.hasCallback()) {

            for (p in methodModel.parameters) {
                if (p.isCallback) {
                    a("        ch.bailu.gtk.Signal.put(0, \"${p.callbackModel.apiName}\", ${p.name});\n")
                }
            }
        }
    }


    private fun getThrowsExtension(methodModel : MethodModel) : String {
        if (methodModel.throwsError()) {
            return "throws ch.bailu.gtk.exception.AllocationError"
        }
        return ""
    }


    private fun getThrowsOnNullSatement(classModel: ClassModel, methodModel : MethodModel) : String {
        val msg = "${classModel.apiName}:${methodModel.apiName}"

        if (methodModel.throwsError()) {
            return "throw new ch.bailu.gtk.exception.AllocationError(\"${msg}\")"
        }
        return "throw new NullPointerException(\"${msg}\")"
    }


    override fun writeInternalConstructor(classModel : ClassModel) {
        start(1)

        a("""
    public ${classModel.getApiName()}(long pointer) {
        super(pointer);
    }
        """)

    }


    override
    fun writeMallocConstructor(classModel : ClassModel) {
        if (classModel.hasDefaultConstructor() == false) {
            start(1)

            a ("""
    public ${classModel.getApiName()}() {
        super(${classModel.getImpName()}.newFromMalloc());
    }
        """)

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
    fun writeSignal(classModel : ClassModel, methodModel : MethodModel) {
        start(1);
        a("    public void ").a(methodModel.getSignalMethodName()).a("(").a(methodModel.getSignalInterfaceName()).a(" observer) {\n");
        a("        ch.bailu.gtk.Signal.put(getCPointer(), \"").a(methodModel.getApiName()).a("\", observer);\n");
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
    fun writeFunction(classModel : ClassModel, methodModel : MethodModel) {
        start(1);
        a("""
    
    public static ${methodModel.getReturnType().getApiType()} ${methodModel.getApiName()}(${getSignature(methodModel.getParameters())}) {
        ${getFunctionCall(classModel, methodModel)};
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
                result.append("${del}${p.getName()}.getCPointer()")
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
                result.append("${p.getName()}.getCPointer()")
            }
            del = ", ";
        }
        return result.toString()
    }
}
