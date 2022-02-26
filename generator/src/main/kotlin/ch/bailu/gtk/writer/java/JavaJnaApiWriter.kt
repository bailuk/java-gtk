package ch.bailu.gtk.writer.java

import ch.bailu.gtk.model.*
import ch.bailu.gtk.model.filter.ModelList
import ch.bailu.gtk.writer.*
import ch.bailu.gtk.writer.java_doc.JavaDoc
import ch.bailu.gtk.writer.java_doc.JavaDocWriter

class JavaJnaApiWriter(private val out: TextWriter, doc: JavaDoc) : CodeWriter {

    private val javaDoc = JavaDocWriter(out, doc)

    companion object {
        fun writeHeader(out: TextWriter, namespaceModel: NamespaceModel) {
            out.a("/* this file is machine generated */\n")
            out.a("package ${namespaceModel.fullNamespace};\n\n")
        }
    }

    override fun writeStart(structureModel : StructureModel, namespaceModel : NamespaceModel) {
        writeHeader(out, namespaceModel)

        out.a("import javax.annotation.Nullable;\n")
        out.a("import javax.annotation.Nonnull;\n")
        out.a("import ch.bailu.gtk.type.Str;\n")
        out.a("import ch.bailu.gtk.type.CPointer;")

        out.end(3)
    }

    override fun writeClass(structureModel : StructureModel) {
        out.start(3)
        //javaDoc.writeClass(structureModel, namespaceModel)
        out.a("public class ${structureModel.apiName} extends ${structureModel.apiParentName} {\n")
        out.end(1)
    }

    override fun writeInterface(structureModel : StructureModel) {
        out.start(3)
        //javaDoc.writeInterface(structureModel)
        out.a("public interface ${structureModel.apiName} {\n")
        out.end(1)
    }

    override fun writeUnsupported(model: Model) {
        out.start(0)
        out.a("    /* Unsupported:${model} */\n")
        out.end(0)
    }


    override fun writeMethod(structureModel : StructureModel, methodModel : MethodModel) {
        out.start(1)
        //javaDoc.writeNativeMethod(structureModel, methodModel)
        writeFunctionCall(structureModel, methodModel, true)
        out.end(1)
    }


    private fun writeFunctionCall(structureModel : StructureModel, methodModel : MethodModel, selfCall: Boolean) {
        val staticToken = if (selfCall) "" else "static "

        out.a("""
            public ${staticToken}${methodModel.returnType.apiType} ${methodModel.apiName}(${getSignature(methodModel.parameters)}) ${getThrowsExtension(methodModel)} {
                ${getFunctionCall(structureModel, methodModel, selfCall)};
            }
            """,4)
    }

    override fun writeFunction(structureModel : StructureModel, methodModel : MethodModel) {
        out.start(1)
        //javaDoc.writeFunction(structureModel, methodModel)
        writeFunctionCall(structureModel, methodModel, false)
        out.end(1)
    }

    private fun getCallSignature(methodModel : MethodModel, selfCall : Boolean) : String {
        return if (selfCall) {
            getSelfCallSignature(methodModel)
        } else {
            getCallSignature(methodModel, "")
        }
    }

    private fun getFunctionCall(c : StructureModel, m : MethodModel, selfCall : Boolean) : String {
        val result = StringBuilder()
        val signature = getCallSignature(m, selfCall)

        if (m.returnType.isVoid) {
            result.append(getFullCall(c, m, signature))

        } else if (m.returnType.isJavaNative) {
            result.append("return ${getFullCall(c, m, signature)}")

        } else {
            result.append("return new ${m.returnType.apiType}(new CPointer(${getFullCall(c, m, signature)}))")
        }
        return result.toString()
    }

    private fun getFullCall(c: StructureModel, m: MethodModel, signature: String) : String {
        return "${c.jnaName}.INST().${m.gtkName}(${signature})"
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
        //javaDoc.writeInternalConstructor(structureModel)
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
            //javaDoc.writeMallocConstructor(structureModel)
            out.a("""
                public ${structureModel.apiName}() {
                    super(toCPointer(new ${structureModel.jnaName}.Fields().getPointer()));
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
        //javaDoc.writeConstructor(structureModel, methodModel)
        out.a("""
            public ${structureModel.apiName}(${getSignature(methodModel.parameters)}) {
                super(new CPointer(${structureModel.jnaName}.INST().${methodModel.gtkName}(${getFactoryCallSignature(methodModel)})));
            }
        """, 4)
        out.end(1)
    }

    override fun writeFactory(structureModel : StructureModel, methodModel : MethodModel) {
        out.start(1)
        //javaDoc.writeFactory(structureModel, methodModel)
        out.a("""
            public static ${structureModel.apiName} ${methodModel.apiName}${structureModel.apiName}(${getSignature(methodModel.parameters)}) ${getThrowsExtension(methodModel)} {
                CPointer pointerToObject = new CPointer(${structureModel.jnaName}.INST().${methodModel.gtkName}(${getFactoryCallSignature(methodModel)}));
               
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
        //javaDoc.writeConstant(parameterModel)

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
        out.a("    public static long getTypeID() { return ${structureModel.jnaName}.INST().${structureModel.typeFunction}(); }\n" )
        out.end(0)
    }

    override fun writeSignal(structureModel : StructureModel, methodModel : MethodModel) {
        out.start(1)

        out.a("""
            public void ${getJavaSignalMethodName(methodModel.name)}(${getJavaSignalInterfaceName(methodModel.name)} signal) {
                ${structureModel.jnaName}.INST().g_signal_connect_data(getCPointer(), new Str("${methodModel.name}").getCPointer(), to${getJavaSignalInterfaceName(methodModel.name)}(signal), 0L, 0L, 0);
            }
        """, 4)
        out.end(1)
    }


    override fun writeCallback(structureModel: StructureModel, methodModel: MethodModel, isSignal: Boolean) {

        val iName = getJavaSignalInterfaceName(methodModel.name)
        val mName = getJavaSignalMethodName(methodModel.name)

        out.start(1)
        out.a("    public interface ${getJavaSignalInterfaceName(methodModel.name)} {\n")

        //javaDoc.writeCallback(structureModel, methodModel)

        out.a("""
                ${methodModel.returnType.apiType} ${mName}(${getSignature(methodModel.parameters)});
            }
            
            private static ${structureModel.jnaName}.${iName} to${iName}(${iName} in) {
                ${structureModel.jnaName}.${iName} out = null;
                if (in != null) {
                    out = (${getCallbackOutSignature(methodModel, isSignal)}) -> in.${mName}${getCallbackInSignature(methodModel)};
                    ch.bailu.gtk.Refs.add(out, in);
                }
                return out;
            }
        """, 4)

        out.end(1)
    }

    private fun getCallbackInSignature(methodModel : MethodModel) : String {
        val result = StringBuilder()

        result.append("(${getSignalInterfaceCallSignature(methodModel)})")

        if (!methodModel.returnType.isVoid && !methodModel.returnType.isJavaNative) {
            result.append(".getCPointer()")
        }
        return result.toString()
    }

    private fun getSignalInterfaceCallSignature(methodModel : MethodModel) : String {
        val result = StringBuilder()
        var del = ""

        for (p in (methodModel).parameters) {
            result.append(del)
            if (p.isJavaNative) {
                result.append("_").append(p.name)
            } else {
                result.append("new ${p.apiType}(new CPointer(_${p.name}))")
            }
            del = ", "
        }
        return result.toString()

    }

    private fun getCallbackOutSignature(methodModel: MethodModel, signal: Boolean): String {
        val result = StringBuilder()

        var del = ""
        if (signal) {
            result.append("__self")
            del = ", "
        }

        methodModel.parameters.forEach {
            result.append("${del}_${it.name}")
            del = ", "
        }

        if (signal) {
            result.append("${del}__data")
        }
        return result.toString()
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


    private fun getSelfCallSignature(methodModel: MethodModel) : String {
        return "getCPointer()${getCallSignature(methodModel, ", ")}"
    }

    private fun getFactoryCallSignature(methodModel: MethodModel) : String {
        return getCallSignature(methodModel, " ")
    }

    private fun getCallSignature(methodModel: MethodModel, firstDel : String) : String{
        val result = StringBuilder()
        var del = firstDel


        for (p in methodModel.parameters) {
            if (p.isCallback && p.callbackModel != null) {
                val iName = getJavaSignalInterfaceName(p.callbackModel.name)
                result.append("${del}to${iName}(${p.name})")
            } else if  (p.isJavaNative) {
                result.append("${del}${p.name}")
            } else if (p.nullable) {
                    result.append("${del}toCPointer(${p.name})")
            } else {
                result.append("${del}toCPointerNotNull(${p.name})")
            }
            del = ", "
        }

        if (methodModel.throwsError) {
            result.append("${del}0L")
        }
        return result.toString()
    }

    override fun writeBeginStruct(structureModel: StructureModel, fields: ModelList<ParameterModel>) {
        out.start(0)
        out.a("""
            private ${structureModel.jnaName}.Fields _fields;

            ${structureModel.jnaName}.Fields toFields() {
                if (_fields == null) {
                    _fields = new ${structureModel.jnaName}.Fields(getCPointer());
                }
                return _fields;
            }
        """, 4)
        out.end(0)
    }

    override fun writeEndStruct() {}
    override fun writeBeginInstace(namespaceModel: NamespaceModel) {}
    override fun writeEndInstance() {}
    override fun writeField(structureModel : StructureModel, parameterModel : ParameterModel) {
        val parameters : MutableList<ParameterModel> = ArrayList()

        val getter = getJavaFieldGetterName(parameterModel.name)
        val setter = getJavaFieldSetterName(parameterModel.name)

        out.start(1)
        //javaDoc.writeField(structureModel, parameterModel)

        if (parameterModel.isJavaNative) {
            out.a("""
                public ${parameterModel.apiType} ${getter}() {
                    toFields().read();
                    return toFields().${parameterModel.name};
                } 
                """, 4)

        } else {
            out.a("""
                public ${parameterModel.apiType} ${getter}() {
                    toFields().read();
                    return new ${parameterModel.apiType}(new CPointer(toFields().${parameterModel.name}));
                }
                """, 4)
        }

        if (parameterModel.isWriteable && !parameterModel.isDirectType) {
            parameters.add(parameterModel)
            if (parameterModel.isJavaNative) {
                out.a("""
                    public void ${setter}(${getSignature(parameters)}) {
                        toFields().${parameterModel.name} = ${parameterModel.name};
                        toFields().write();
                    }
                """, 4)
            } else {
                out.a("""
                    public void ${setter}(${getSignature(parameters)}) {
                        toFields().${parameterModel.name} = ${parameterModel.name}.getCPointer();
                        toFields().write();
                    }
                """, 4)
            }
        }
        out.end(1)
    }
}
