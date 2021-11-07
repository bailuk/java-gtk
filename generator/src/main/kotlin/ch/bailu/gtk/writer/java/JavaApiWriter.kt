package ch.bailu.gtk.writer.java

import ch.bailu.gtk.model.*
import ch.bailu.gtk.writer.*
import ch.bailu.gtk.writer.java_doc.JavaDoc
import ch.bailu.gtk.writer.java_doc.JavaDocWriter
import java.io.Writer


class JavaApiWriter(writer: Writer, doc: JavaDoc) : CodeWriter(writer) {

    private val javaDoc = JavaDocWriter(writer, doc)

    override fun writeStart(structureModel : StructureModel, namespaceModel : NamespaceModel) {
        start(3)
        a("package ${namespaceModel.getFullNamespace()};\n\n")
        a("import javax.annotation.Nullable;\n")
        a("import javax.annotation.Nonnull;")
        end(3)
    }


    override fun writeClass(structureModel : StructureModel) {
        start()
        javaDoc.writeClass(structureModel)
        a("public class ${structureModel.apiName} extends ${structureModel.apiParentName} {\n")
    }

    override fun writeInterface(structureModel : StructureModel) {
        start()
        javaDoc.writeInterface(structureModel)
        a("public interface ${structureModel.apiName} {\n")
    }

    override fun writeUnsupported(model: Model) {
        start (1)
        a ("    /* Unsupported:${model} */\n")
    }


    override fun writeNativeMethod(structureModel : StructureModel, methodModel : MethodModel) {
        start(2)
        javaDoc.writeNativeMethod(structureModel, methodModel)
        writeFunctionCall(structureModel, methodModel, true)
        next()
    }


    private fun writeFunctionCall(structureModel : StructureModel, methodModel : MethodModel, selfCall: Boolean) {
        val staticToken = if (selfCall) {
            ""
        } else {
            "static "
        }

        a("""
            public ${staticToken}${methodModel.returnType.apiType} ${methodModel.apiName}(${getSignature(methodModel.getParameters())}) ${getThrowsExtension(methodModel)} {
                ${getCallbackConnections(methodModel)}
                ${getFunctionCall(structureModel, methodModel, selfCall)};
            }
            """.replaceIndent(" ".repeat(4)))
    }

    private fun getEmitter(methodModel: MethodModel) : String {
        return try {
            val last = methodModel.getParameters().last {
                it.apiType == "ch.bailu.gtk.type.Pointer"
            }

            "toCPointer(${last.name})"
        } catch (e : NoSuchElementException) {
            "0"
        }
    }

    override fun writeFunction(structureModel : StructureModel, methodModel : MethodModel) {
        start(2)
        javaDoc.writeFunction(structureModel, methodModel)
        writeFunctionCall(structureModel, methodModel, false)
        next()
    }

    private fun getCallSignature(m : MethodModel, selfCall : Boolean) : String {
        return if (selfCall) {
            getSelfCallSignature(m.getParameters());
        } else {
            getCallSignature(m.getParameters(), "")
        }
    }

    private fun getFunctionCall(c : StructureModel, m : MethodModel, selfCall : Boolean) : String {
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
        val del = " ".repeat(16)
        if (methodModel.hasCallback()) {
            result.append("final long emitter = ${getEmitter(methodModel)};\n")

            for (p in methodModel.getParameters()) {
                if (p.isCallback) {
                    result.append("${del}ch.bailu.gtk.Callback.put(emitter, \"${p.callbackModel?.apiName}\", ${p.name});\n")
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


    private fun getThrowsOnNullSatement(structureModel: StructureModel, methodModel : MethodModel) : String {
        val msg = "${structureModel.apiName}:${methodModel.apiName}"

        return if (methodModel.throwsError) {
            "throw new ch.bailu.gtk.exception.AllocationError(\"${msg}\")"
        } else {
            "throw new NullPointerException(\"${msg}\")"
        }
    }


    override fun writeInternalConstructor(structureModel : StructureModel) {
        start(1)
        javaDoc.writeInternalConstructor(structureModel)
        a("""
            public ${structureModel.apiName}(long pointer) {
                super(pointer);
            }
        """.replaceIndent(" ".repeat(4)))
        end(1)
    }


    override fun writeMallocConstructor(structureModel : StructureModel) {
        if (structureModel.hasDefaultConstructor() == false) {
            start(1)
            javaDoc.writeMallocConstructor(structureModel)
            a ("""
                
                public ${structureModel.apiName}() {
                    super(${structureModel.impName}.newFromMalloc());
                }
                
                public void destroy() {
                    ch.bailu.gtk.type.ImpUtil.destroy(getCPointer());
                }
            """, 4)

        }
    }

    override fun writeConstructor(structureModel : StructureModel, methodModel : MethodModel) {
        start(1);
        javaDoc.writeConstructor(structureModel, methodModel)
        a("""
            public ${structureModel.apiName}(${getSignature(methodModel.getParameters())}) {
                super(${structureModel.impName}.${methodModel.apiName}(${getFactoryCallSignature(methodModel.getParameters())}));
            }
        """, 4)
        end(1)
    }

    override fun writeFactory(structureModel : StructureModel, methodModel : MethodModel) {
        start(1);
        javaDoc.writeFactory(structureModel, methodModel)
        a("""
            public static ${structureModel.apiName} ${methodModel.apiName}${structureModel.apiName}(${getSignature(methodModel.getParameters())}) ${getThrowsExtension(methodModel)} {
                long pointerToObject = ${structureModel.impName}.${methodModel.apiName}(${getFactoryCallSignature(methodModel.getParameters())});
               
                if (pointerToObject == 0) {
                    ${getThrowsOnNullSatement(structureModel, methodModel)};
                }
        
                return new ${structureModel.apiName}(pointerToObject);
            }        

        """, 4)
        end(1)
    }

    override
    fun writePrivateFactory(structureModel : StructureModel, methodModel : MethodModel) {}


    override fun writeConstant(parameterModel : ParameterModel) {
        start(1)
        javaDoc.writeConstant(parameterModel)

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
        start(1)
        a("}\n")
    }

    override  fun writeSignal(structureModel : StructureModel, methodModel : MethodModel) {
        start(1);
        a("    public void ").a(getJavaSignalMethodName(methodModel.name)).a("(").a(
            getJavaSignalInterfaceName(methodModel.name)
        ).a(" observer) {\n");
        a("        ch.bailu.gtk.Callback.put(getCPointer(), \"").a(methodModel.apiName).a("\", observer);\n");
        a("        ").a(structureModel.impName).a(".").a(getJavaSignalMethodName(methodModel.name)).a("(getCPointer());\n");
        a("    }\n");
        a("    public interface ").a(getJavaSignalInterfaceName(methodModel.name)).a(" {\n")

        javaDoc.writeSignal(structureModel, methodModel)

        a("        ").a(methodModel.returnType.apiType).a(" ").a(getJavaSignalMethodName(methodModel.name)); writeSignature(methodModel); a(";\n");
        a("    }\n");
    }


    override fun writeCallback(structureModel: StructureModel, methodModel: MethodModel) {
        start(1)
        a("    public interface ${getJavaSignalInterfaceName(methodModel.name)} {\n")

        javaDoc.writeCallback(structureModel, methodModel)

        a("        ${methodModel.returnType.apiType} ${getJavaSignalMethodName(methodModel.name)}(${getSignature(methodModel.getParameters())});\n")
        a("    }\n")
    }

    override fun writeField(structureModel : StructureModel, parameterModel : ParameterModel) {
        val parameters : MutableList<ParameterModel> = ArrayList()

        val getter = getJavaFieldGetterName(parameterModel.name)
        val setter = getJavaFieldSetterName(parameterModel.name)

        start(1)
        javaDoc.writeField(structureModel, parameterModel)

        if (parameterModel.isJavaNative) {
            a("""
                
    public ${parameterModel.apiType} ${getter}() {
        return ${structureModel.impName}.${getter}(${getSelfCallSignature(parameters)});
    } 
    """)

        } else {
            a("""
                
    public ${parameterModel.apiType} ${getter}() {
        return new ${parameterModel.apiType}(${structureModel.impName}.${getter}(${getSelfCallSignature(parameters)}));
    }
    """)

        }

        if (parameterModel.isWriteable && !parameterModel.isDirectType) {
            parameters.add(parameterModel)
            a("""
                
    public void ${setter}(${getSignature(parameters)}) {        
        ${structureModel.impName}.${setter}(${getSelfCallSignature(parameters)});
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
