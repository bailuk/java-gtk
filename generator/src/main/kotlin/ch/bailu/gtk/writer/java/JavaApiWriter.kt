package ch.bailu.gtk.writer.java

import ch.bailu.gtk.model.*
import ch.bailu.gtk.model.filter.ModelList
import ch.bailu.gtk.validator.Validator
import ch.bailu.gtk.writer.*
import ch.bailu.gtk.writer.java_doc.JavaDoc
import ch.bailu.gtk.writer.java_doc.JavaDocWriter

class JavaApiWriter(private val out: TextWriter, doc: JavaDoc) : CodeWriter {

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
        javaDoc.writeClass(structureModel)

        Validator.validateAlias(structureModel.apiParentName)

        out.a("public class ${structureModel.apiName} extends ${structureModel.apiParentName} {\n")
        out.a("    public static ch.bailu.gtk.lib.handler.ClassHandler getClassHandler() {\n")
        out.a("        return ch.bailu.gtk.lib.handler.ClassHandler.get(${structureModel.apiName}.class.getCanonicalName());\n")
        out.a("    }\n")
        out.end(1)
    }

    override fun writeInterface(structureModel : StructureModel) {
        out.start(3)
        javaDoc.writeInterface(structureModel)
        out.a("public interface ${structureModel.apiName} {\n")
        out.end(1)
    }

    override fun writeDebugBegin(structureModel: StructureModel) {
        out.start(1)
        out.a("/*").nl()
        out.a("type-").a(structureModel.structureType.value).nl()
        if (structureModel.disguised) out.a("flag-disguised").nl()
        if (structureModel.allFieldsAreSupported && structureModel.isRecord) out.a("all-fields-supported").nl()
        if (!structureModel.allFieldsAreSupported && structureModel.isRecord) out.a("some-fields-unsupported").nl()
        out.end(1)
    }

    override fun writeDebugUnsupported(model: Model) {
        out.start(1)
        out.a(model.toString()).nl()
        out.end(1)
    }

    override fun writeDebugEnd() {
        out.a("*/\n")
    }

    override fun writeImplements(implementsModel: ImplementsModel) {
        Validator.validateNotEmpty(implementsModel.apiTypeName)
        Validator.validateNotEmpty(implementsModel.name)
        Validator.validateSupported(implementsModel)

        val type = implementsModel.apiTypeName
        val name = implementsModel.name

        out.start(1)
        javaDoc.writeImplements(implementsModel)
        out.a("    public $type as${name}() {\n")
        out.a("        return new ${type}(cast());\n")
        out.a("    }\n")
        out.end(1)
    }

    override fun writeMethod(structureModel : StructureModel, methodModel : MethodModel) {
        out.start(1)
        javaDoc.writeMethod(structureModel, methodModel)
        writeFunctionCall(structureModel, methodModel, "this", "getCPointer()")
        out.end(1)
    }

    override fun writeFunction(structureModel : StructureModel, methodModel : MethodModel) {
        out.start(1)
        javaDoc.writeFunction(structureModel, methodModel)
        writeFunctionCall(structureModel, methodModel, "getClassHandler().getInstance()", "", "static ")
        out.end(1)
    }

    private fun writeFunctionCall(structureModel : StructureModel, methodModel : MethodModel, self: String, prefix: String, staticToken: String = "") {
        Validator.validMethodName(methodModel)

        out.a("""
            public ${staticToken}${methodModel.returnType.getApiTypeName(structureModel.nameSpaceModel.namespace)} ${methodModel.apiName}(${getSignature(structureModel, methodModel.parameters)}) ${getThrowsExtension(methodModel)} {
                ${getFunctionCall(structureModel, methodModel, prefix, self)};
            }
            """,4)
    }

    private fun getFunctionCall(c : StructureModel, m : MethodModel, prefix: String, self: String) : String {
        val result = StringBuilder()
        val signature = getCallSignature(m, prefix, self)

        if (m.returnType.isVoid) {
            result.append(getFullCall(c, m, signature))

        } else if (m.returnType.isJavaNative) {
            result.append("return ${getFullCall(c, m, signature)}")

        } else {
            result.append("return new ${m.returnType.getApiTypeName(c.nameSpaceModel.namespace)}(new CPointer(${getFullCall(c, m, signature)}))")
        }
        return result.toString()
    }

    private fun getFullCall(c: StructureModel, m: MethodModel, signature: String) : String {
        return "${c.jnaName}.INST().${m.gtkName}(${signature})"
    }


    private fun getThrowsExtension(methodModel : MethodModel) : String {
        return if (methodModel.throwsError) {
            "throws ch.bailu.gtk.type.exception.AllocationError"
        } else {
            ""
        }
    }


    private fun getThrowsOnNullStatement(structureModel: StructureModel, methodModel : MethodModel) : String {
        val msg = "${structureModel.apiName}:${methodModel.apiName}"

        return if (methodModel.throwsError) {
            "throw new ch.bailu.gtk.type.exception.AllocationError(\"${msg}\")"
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
                    super(toCPointer(${structureModel.jnaName}.allocateStructure()));
                }
            """, 4)
            out.end(1)
        }
    }

    override fun writeConstructor(structureModel : StructureModel, methodModel : MethodModel) {
        out.start(1)
        javaDoc.writeConstructor(structureModel, methodModel)
        out.a("""
            public ${structureModel.apiName}(${getSignature(structureModel, methodModel.parameters)}) {
                super(new CPointer(${structureModel.jnaName}.INST().${methodModel.gtkName}(${getCallSignature(methodModel, "", "getClassHandler().getInstance()")})));
            }
        """, 4)
        out.end(1)
    }

    override fun writeFactory(structureModel : StructureModel, methodModel : MethodModel) {
        out.start(1)
        javaDoc.writeFactory(structureModel, methodModel)
        out.a("""
            public static ${structureModel.apiName} ${methodModel.apiName}${structureModel.apiName}(${getSignature(structureModel, methodModel.parameters)}) ${getThrowsExtension(methodModel)} {
                CPointer __self = new CPointer(${structureModel.jnaName}.INST().${methodModel.gtkName}(${getCallSignature(methodModel, "", "getClassHandler().getInstance()")}));
                if (__self.isNull()) {
                    ${getThrowsOnNullStatement(structureModel, methodModel)};
                }
                return new ${structureModel.apiName}(__self);
            }        

        """, 4)
        out.end(1)
    }

    override
    fun writePrivateFactory(structureModel : StructureModel, methodModel : MethodModel) {}


    override fun writeConstant(structureModel: StructureModel, parameterModel : ParameterModel) {
        out.start(0)
        javaDoc.writeConstant(structureModel, parameterModel)

        var value = parameterModel.value
        var type  = parameterModel.getApiTypeName(structureModel.nameSpaceModel.namespace)

        if (parameterModel.getApiTypeName(structureModel.nameSpaceModel.namespace).endsWith("Str")) {
            type = "String"
            value = "\"$value\""
        }

        out.a("    " + type + " " + parameterModel.name + " = " + value + ";\n")
        out.end(1)
    }

    override fun writeClassEnd() {
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
            public final static String ${methodModel.signalNameVariable} = "${methodModel.name}";
            
        """, 4)

        javaDoc.writeSignal(structureModel, methodModel)
        out.a("""
            public ch.bailu.gtk.lib.handler.SignalHandler ${Names.getJavaCallbackMethodName(methodModel.name)}(${Names.getJavaCallbackInterfaceName(methodModel.name)} signal) {
                return connectSignal(${methodModel.signalNameVariable}, to${Names.getJavaCallbackInterfaceName(methodModel.name)}(signal));
            }
        """, 4)
        out.end(1)
    }


    override fun writeCallback(structureModel: StructureModel, methodModel: MethodModel, isSignal: Boolean) {
        if (methodModel.isPublic) {
            if (isSignal) {
                writeSignalInterface(structureModel, methodModel)
            } else {
                writeCallbackInterface(structureModel, methodModel)
            }
        }
    }

    private fun writeSignalInterface(structureModel: StructureModel, methodModel: MethodModel) {
        val iName = Names.getJavaCallbackInterfaceName(methodModel.name)
        val mName = Names.getJavaCallbackMethodName(methodModel.name)

        out.start(1)
        out.a("    @FunctionalInterface\n")
        out.a("    public interface ${Names.getJavaCallbackInterfaceName(methodModel.name)} {\n")

        javaDoc.writeCallback(structureModel, methodModel)

        out.a("""
                ${methodModel.returnType.getApiTypeName(structureModel.nameSpaceModel.namespace)} ${mName}(${getSignature(structureModel, methodModel.parameters)});
            }
            
            private static com.sun.jna.Callback to${iName}(${iName} in) {
                return (in == null) ? null: (${structureModel.jnaName}.${iName}) (${getCallbackOutSignature(methodModel, "__self", "__data")}) -> in.${mName}${getCallbackInSignature(structureModel, methodModel)};
            }
        """, 4)

        out.end(1)
    }

    private fun writeCallbackInterface(structureModel: StructureModel, methodModel: MethodModel) {

        val iName = Names.getJavaCallbackInterfaceName(methodModel.name)
        val mName = Names.getJavaCallbackMethodName(methodModel.name)

        out.start(1)
        out.a("    @FunctionalInterface\n")
        out.a("    public interface ${Names.getJavaCallbackInterfaceName(methodModel.name)} {\n")

        javaDoc.writeCallback(structureModel, methodModel)

        out.a("""
                ${methodModel.returnType.getApiTypeName(structureModel.nameSpaceModel.namespace)} ${mName}(${getSignature(structureModel, methodModel.parameters, "ch.bailu.gtk.lib.handler.CallbackHandler __self")});
            }
            
            private static ${structureModel.jnaName}.${iName} to${iName}(ch.bailu.gtk.type.Pointer instance, String methodName, $iName in) {
                ${structureModel.jnaName}.${iName} out = null;
                if (in != null) {
                    ch.bailu.gtk.lib.handler.CallbackHandler __callback = new ch.bailu.gtk.lib.handler.CallbackHandler(instance, methodName);
                    out = (${getCallbackOutSignature(methodModel)}) -> in.${mName}${getCallbackInSignature(structureModel, methodModel, "__callback")};
                    __callback.register(out);
                }
                return out;
            }
        """, 4)

        out.end(1)
    }

    private fun getCallbackInSignature(structureModel: StructureModel, methodModel : MethodModel, prefix: String = "") : String {
        val result = StringBuilder()

        result.append("(${getSignalInterfaceCallSignature(structureModel, methodModel, prefix)})")

        if (!methodModel.returnType.isVoid && !methodModel.returnType.isJavaNative) {
            result.append(".getCPointer()")
        }
        return result.toString()
    }

    private fun getSignalInterfaceCallSignature(structureModel: StructureModel, methodModel : MethodModel, prefix : String = "") : String {
        val result = StringBuilder(prefix)
        var del = if (prefix.isNotEmpty()) ", " else ""

        for (p in (methodModel).parameters) {
            result.append(del)
            if (p.isJavaNative) {
                result.append("_").append(p.name)
            } else {
                result.append("new ${p.getApiTypeName(structureModel.nameSpaceModel.namespace)}(new CPointer(_${p.name}))")
            }
            del = ", "
        }
        return result.toString()
    }

    private fun getCallbackOutSignature(methodModel: MethodModel, prefix: String = "", postfix: String = ""): String {
        val result = StringBuilder(prefix)

        var del = if (prefix.isEmpty()) "" else ", "

        methodModel.parameters.forEach {
            result.append("${del}_${it.name}")
            del = ", "
        }

        if (methodModel.throwsError) {
            result.append("${del}_error")
        }

        if (postfix.isNotEmpty()) {
            result.append("${del}${postfix}")
        }

        return result.toString()
    }

    private fun getSignature(structureModel: StructureModel, fieldModel: FieldModel) : String{
        return "${fieldModel.getApiTypeName(structureModel.nameSpaceModel.namespace)} ${fieldModel.name}"
    }

    private fun getSignature(structureModel: StructureModel, parameters : List<ParameterModel>, prefix: String = "") : String{
        val result = StringBuilder(prefix)
        var del = if (prefix.isEmpty()) "" else ", "

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

            result.append("${del}${nonnull}${p.getApiTypeName(structureModel.nameSpaceModel.namespace)} ${p.name}")
            del = ", "
        }
        return result.toString()
    }

    private fun getCallSignature(methodModel: MethodModel, prefix : String, self: String) : String{
        val result = StringBuilder(prefix)
        var del = if (prefix.isEmpty()) "" else ", "


        for (p in methodModel.parameters) {
            if (p.isCallback && p.callbackModel != null) {
                val iName = Names.getJavaCallbackInterfaceName(p.callbackModel.name)
                result.append("${del}to${iName}(${self}, \"${methodModel.apiName}\", ${p.name})")
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

    override fun writeBeginStruct(structureModel: StructureModel, fields: ModelList<FieldModel>) {
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
    override fun writeBeginInstance(namespaceModel: NamespaceModel) {}
    override fun writeEndInstance() {}
    override fun writeField(structureModel : StructureModel, fieldModel : FieldModel) {
        Validator.validateSupported(fieldModel)
        Validator.validateSupported(structureModel)
        if (fieldModel.isMethod && fieldModel.isPublic) {
            Validator.validateSupported(fieldModel.methodModel)
        }

        val fieldName = fieldModel.name
        val fieldNameConst = Names.getJavaConstantName(fieldName)

        writeFieldName(structureModel, fieldModel, fieldName, fieldNameConst)

        if (fieldModel.isPublic) {
            if (fieldModel.isWriteable && !fieldModel.isDirectType) {
                writeFieldWrite(structureModel, fieldModel, fieldName, fieldNameConst)
            }

           writeFieldRead(structureModel, fieldModel, fieldName, fieldNameConst)
        }
    }

    private fun writeFieldWrite(structureModel: StructureModel, fieldModel: FieldModel, fieldName: String, fieldNameConst: String) {
        val value = if (fieldModel.isMethod) {
            val iName = fieldModel.getApiTypeName(structureModel.nameSpaceModel.namespace)
            "to$iName(this, $fieldNameConst, $fieldName)"
        } else if (fieldModel.isJavaNative) {
            fieldName
        } else {
            "$fieldName.getCPointer()"
        }
        val setter = Names.getJavaFieldSetterName(fieldModel.name)

        out.start(1)
        javaDoc.writeField(structureModel, fieldModel)
        out.a(
            """
                public void ${setter}(${getSignature(structureModel, fieldModel)}) {
                    toFields().$fieldName = $value;
                    toFields().writeField($fieldNameConst);
                }
                """, 4
        )
        out.end(1)
    }

    private fun writeFieldRead(structureModel: StructureModel, fieldModel: FieldModel, fieldName: String, fieldNameConst: String) {
        val jnaPrefix = if (fieldModel.isMethod) {
            "${structureModel.jnaName}."
        } else {
            ""
        }

        val result = if (fieldModel.isMethod || fieldModel.isJavaNative) {
            "toFields().$fieldName"
        } else {
            "new ${fieldModel.getApiTypeName(structureModel.nameSpaceModel.namespace)}(new CPointer(toFields().$fieldName))"
        }

        val getter = Names.getJavaFieldGetterName(fieldModel.name)

        out.start(1)
        javaDoc.writeField(structureModel, fieldModel)
        out.a(
            """
                 public ${jnaPrefix}${fieldModel.getApiTypeName(structureModel.nameSpaceModel.namespace)} ${getter}() {
                    toFields().readField($fieldNameConst);
                    return $result;
                 } 
                 """, 4
        )
        out.end(1)
    }

    private fun writeFieldName(structureModel: StructureModel, fieldModel: FieldModel, fieldName: String, fieldNameConst: String) {
        out.start(1)
        javaDoc.writeField(structureModel, fieldModel)
        out.a("""
            public static final String $fieldNameConst = "$fieldName";
        """, 4)
        out.end(1)
    }
}
