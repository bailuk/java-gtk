package ch.bailu.gtk.writer.java

import ch.bailu.gtk.model.*
import ch.bailu.gtk.writer.*
import ch.bailu.gtk.writer.java_doc.JavaDoc
import ch.bailu.gtk.writer.java_doc.JavaDocWriter


class JavaApiWriter(writer: TextWriter, doc: JavaDoc) : CodeWriter(writer) {

    private val javaDoc = JavaDocWriter(writer, doc)

    override fun writeStart(structureModel : StructureModel, namespaceModel : NamespaceModel) {
        super.writeStart(structureModel, namespaceModel)
        out.a("package ${namespaceModel.fullNamespace};\n\n")
        out.a("import javax.annotation.Nullable;\n")
        out.a("import javax.annotation.Nonnull;\n\n")
        out.a("import ch.bailu.gtk.type.CPointer;")


        out.end(3)
    }


    override fun writeClass(structureModel : StructureModel, namespaceModel: NamespaceModel) {
        out.start(3)
        javaDoc.writeClass(structureModel, namespaceModel)
        out.a("public class ${structureModel.apiName} extends ${structureModel.apiParentName} {\n")
        out.end(1)
    }

    override fun writeInterface(structureModel : StructureModel) {
        out.start(3)
        javaDoc.writeInterface(structureModel)
        out.a("public interface ${structureModel.apiName} {\n")
        out.end(1)
    }

    override fun writeUnsupported(model: Model) {
        out.start(0)
        out.a("    /* Unsupported:${model} */\n")
        out.end(0)
    }


    override fun writeNativeMethod(structureModel : StructureModel, methodModel : MethodModel) {
        out.start(1)
        javaDoc.writeNativeMethod(structureModel, methodModel)
        writeFunctionCall(structureModel, methodModel, true)
        out.end(1)
    }


    private fun writeFunctionCall(structureModel : StructureModel, methodModel : MethodModel, selfCall: Boolean) {
        val staticToken = if (selfCall) "" else "static "

        out.a("""
            public ${staticToken}${methodModel.returnType.apiType} ${methodModel.apiName}(${getSignature(methodModel.parameters)}) ${getThrowsExtension(methodModel)} {
                ${getCallbackConnections(methodModel)}
                ${getFunctionCall(structureModel, methodModel, selfCall)};
            }
            """,4)
    }

    override fun writeFunction(structureModel : StructureModel, methodModel : MethodModel) {
        out.start(1)
        javaDoc.writeFunction(structureModel, methodModel)
        writeFunctionCall(structureModel, methodModel, false)
        out.end(1)
    }

    private fun getCallSignature(m : MethodModel, selfCall : Boolean) : String {
        return if (selfCall) {
            getSelfCallSignature(m.parameters)
        } else {
            getCallSignature(m.parameters, "")
        }
    }

    private fun getFunctionCall(c : StructureModel, m : MethodModel, selfCall : Boolean) : String {
        val result = StringBuilder()
        val signature = getCallSignature(m, selfCall)

        if (m.returnType.isVoid) {
            result.append("${c.impName}.${m.apiName}(${signature})")

        } else if (m.returnType.isJavaNative) {
            result.append("return ${c.impName}.${m.apiName}(${signature})")

        } else {
            result.append("return new ${m.returnType.apiType}(new CPointer(${c.impName}.${m.apiName}(${signature})))")
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


    private fun getThrowsOnNullStatement(structureModel: StructureModel, methodModel : MethodModel) : String {
        val msg = "${structureModel.apiName}:${methodModel.apiName}"

        return if (methodModel.throwsError) {
            "throw new ch.bailu.gtk.exception.AllocationError(\"${msg}\")"
        } else {
            "throw new NullPointerException(\"${msg}\")"
        }
    }


    override fun writeInternalConstructor(structureModel : StructureModel) {
        out.start(1)
        javaDoc.writeInternalConstructor(structureModel)
        out.a("""
            public ${structureModel.apiName}(CPointer pointer) {
                super(pointer);
            }
        """,4)
        out.end(1)
    }


    override fun writeMallocConstructor(structureModel : StructureModel) {
        if (!structureModel.hasDefaultConstructor()) {
            out.start(1)
            javaDoc.writeMallocConstructor(structureModel)
            out.a("""
                public ${structureModel.apiName}() {
                    super(new CPointer(${structureModel.impName}.newFromMalloc()));
                }
                
                public void destroy() {
                    ch.bailu.gtk.type.ImpUtil.destroy(getCPointer());
                }
            """, 4)
            out.end(1)
        }
    }

    override fun writeConstructor(structureModel : StructureModel, methodModel : MethodModel) {
        out.start(1)
        javaDoc.writeConstructor(structureModel, methodModel)
        out.a("""
            public ${structureModel.apiName}(${getSignature(methodModel.parameters)}) {
                super(new CPointer(${structureModel.impName}.${methodModel.apiName}(${getFactoryCallSignature(methodModel.parameters)})));
                ${getCallbackConnections(methodModel)}
            }
        """, 4)
        out.end(1)
    }

    override fun writeFactory(structureModel : StructureModel, methodModel : MethodModel) {
        out.start(1)
        javaDoc.writeFactory(structureModel, methodModel)
        out.a("""
            public static ${structureModel.apiName} ${methodModel.apiName}${structureModel.apiName}(${getSignature(methodModel.parameters)}) ${getThrowsExtension(methodModel)} {
                CPointer pointerToObject = new CPointer(${structureModel.impName}.${methodModel.apiName}(${getFactoryCallSignature(methodModel.parameters)}));
               
                if (pointerToObject.isNull()) {
                    ${getThrowsOnNullStatement(structureModel, methodModel)};
                }
        
                return new ${structureModel.apiName}(pointerToObject);
            }        

        """, 4)
        out.end(1)
    }

    override
    fun writePrivateFactory(structureModel : StructureModel, methodModel : MethodModel) {}


    override fun writeConstant(parameterModel : ParameterModel) {
        out.start(0)
        javaDoc.writeConstant(parameterModel)

        var value = parameterModel.value
        var type  = parameterModel.apiType

        if (parameterModel.apiType.endsWith("Str")) {
            type = "String"
            value = "\"$value\""
        } else if ("true" == value){
            value = "ch.bailu.gtk.GTK.TRUE"

        } else if ("false" == value){
            value = "ch.bailu.gtk.GTK.FALSE"
        }

        out.a("    " + type + " " + parameterModel.name + " = " + value + ";\n")
        out.end(0)
    }

    override fun writeEnd() {
        out.start(0)
        out.a("}\n")
        out.end(0)
    }

    override fun writeGetTypeFunction(structureModel: StructureModel) {
        out.start(1)
        out.a("    public static long getTypeID() { return " + structureModel.impName + ".getTypeID(); }\n" )
        out.end(0)
    }

    override fun writeSignal(structureModel : StructureModel, methodModel : MethodModel) {
        out.start(1)

        out.a("""
            public void ${getJavaSignalMethodName(methodModel.name)}(${getJavaSignalInterfaceName(methodModel.name)} observer) {
                ch.bailu.gtk.Callback.put(getCPointer(), "${methodModel.name}", observer);
                ${structureModel.impName}.${getJavaSignalMethodName(methodModel.name)}(getCPointer());
            }
            
            public interface ${getJavaSignalInterfaceName(methodModel.name)} {
        """, 4)
        javaDoc.writeSignal(structureModel, methodModel)
        out.a("""          
                ${methodModel.returnType.apiType} ${getJavaSignalMethodName(methodModel.name)}(${getSignature(methodModel.parameters)});
            }
        """, 4)
        out.end(1)
    }


    private fun getCallbackConnections (methodModel: MethodModel) : String {
        val result = StringBuilder()
        val del = " ".repeat(16)
        if (methodModel.hasCallback()) {
            result.append("final long emitter = ${emitterIdFromModel(methodModel) { "toCPointer(${it.name})" }};\n")

            for (p in methodModel.parameters) {
                if (p.isCallback) {
                    result.append("${del}ch.bailu.gtk.Callback.put(emitter, \"${p.callbackModel?.apiName}\", ${p.name});\n")
                }
            }
        }
        return result.toString()
    }

    override fun writeCallback(structureModel: StructureModel, methodModel: MethodModel) {
        out.start(1)
        out.a("    public interface ${getJavaSignalInterfaceName(methodModel.name)} {\n")

        javaDoc.writeCallback(structureModel, methodModel)

        out.a("        ${methodModel.returnType.apiType} ${getJavaSignalMethodName(methodModel.name)}(${getSignature(methodModel.parameters)});\n")
        out.a("    }\n")
        out.end(1)
    }

    override fun writeField(structureModel : StructureModel, parameterModel : ParameterModel) {
        val parameters : MutableList<ParameterModel> = ArrayList()

        val getter = getJavaFieldGetterName(parameterModel.name)
        val setter = getJavaFieldSetterName(parameterModel.name)

        out.start(1)
        javaDoc.writeField(structureModel, parameterModel)

        if (parameterModel.isJavaNative) {
            out.a("""
                public ${parameterModel.apiType} ${getter}() {
                    return ${structureModel.impName}.${getter}(${getSelfCallSignature(parameters)});
                } 
                """, 4)

        } else {
            out.a("""
                public ${parameterModel.apiType} ${getter}() {
                    return new ${parameterModel.apiType}(new CPointer(${structureModel.impName}.${getter}(${getSelfCallSignature(parameters)})));
                }
                """, 4)
            }

        if (parameterModel.isWriteable && !parameterModel.isDirectType) {
            parameters.add(parameterModel)
            out.a("""
                public void ${setter}(${getSignature(parameters)}) {        
                    ${structureModel.impName}.${setter}(${getSelfCallSignature(parameters)});
                }
                """, 4)
        }
        out.end(1)
    }


    private fun getSignature(parameters : List<ParameterModel>) : String{
        val result = StringBuilder()

        var del = ""
        for (p in parameters) {
            val nonnull = if (!p.isJavaNative) {
                if (p.nullable) {
                    "@Nullable "
                } else {
                    "@Nonnull "
                }
            } else {
                ""
            }

            result.append("${del}${nonnull}${p.apiType} ${p.name}")
            del = ", "
        }
        return result.toString()
    }


    private fun getSelfCallSignature(parameters : List<ParameterModel>) : String {
        return "getCPointer()${getCallSignature(parameters, ", ")}"
    }

    private fun getFactoryCallSignature(parameters : List<ParameterModel>) : String {
        return getCallSignature(parameters, " ")
    }

    private fun getCallSignature(parameters : List<ParameterModel>, firstDel : String) : String{
        val result = StringBuilder()
        var del = firstDel

        for (p in parameters) {
            if (p.isJavaNative) {
                result.append("${del}${p.name}")
                del = ", "

            } else if (!p.isCallback) {
                if (p.nullable) {
                    result.append("${del}toCPointer(${p.name})")
                } else {
                    result.append("${del}toCPointerNotNull(${p.name})")
                }
                del = ", "
            }

        }
        return result.toString()
    }
}
