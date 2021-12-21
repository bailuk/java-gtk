package ch.bailu.gtk.writer

import ch.bailu.gtk.model.NamespaceModel
import ch.bailu.gtk.model.StructureModel
import ch.bailu.gtk.model.MethodModel
import ch.bailu.gtk.model.Model
import ch.bailu.gtk.model.ParameterModel
import java.io.Writer


class CWriter (writer : TextWriter) : CodeWriter(writer) {

    override fun writeStart(structureModel : StructureModel, namespaceModel : NamespaceModel) {
        super.writeStart(structureModel, namespaceModel)
        out.a("\n#include <jni.h>\n")

        for (include in namespaceModel.getIncludes()) {
            out.a("#include <${include}>\n")
        }

        out.a("#include \"${getJniHeaderFileName(structureModel)}\"\n\n\n")

        out.a("jclass ${getGlobalClassName(structureModel)};\n")
        out.a("JavaVM* ${getGlobalVMName(structureModel)};\n")

        out.end(3)
    }


    override fun writeNativeMethod(structureModel : StructureModel, methodModel : MethodModel) {
        out.start(2)
        _writeNativeMethod(structureModel, methodModel, true)
        out.end(2)
    }


    override fun writePrivateFactory(structureModel : StructureModel, methodModel : MethodModel) {
        out.start(2)
        _writeNativeMethod(structureModel, methodModel, false);
        out.end(2)
    }

    override fun writeConstant(parameterModel: ParameterModel) {
        
    }

    private fun _writeNativeMethod(structureModel : StructureModel, methodModel : MethodModel, self : Boolean) {
        out.a("""
            JNIEXPORT ${methodModel.returnType.jniType} JNICALL ${getJniMethodName(structureModel, methodModel)}(${getJniSignature(methodModel, self)})
            {
                ${getAllocateParameters(structureModel, methodModel)}
                ${getEnvironmentInit(structureModel, methodModel, false)}
                ${getReturnStatement(methodModel)} ${methodModel.gtkName}(${getGtkCallSignature(structureModel, methodModel, self)});
                ${getFreeParameters(methodModel)}
            }
            """, 0)
    }


    fun getJniSignature(methodModel : MethodModel, self : Boolean) : String {
        val result = StringBuilder()
        result.append("JNIEnv * _jenv, jclass _jself")

        if (self) {
            result.append(", jlong _self")
        }

        for (p in methodModel.getParameters()) {
            if (!p.isCallback) {
                result.append(", ${p.jniType} ${p.name}")
            }
        }
        return result.toString()
    }


    private fun getAllocateParameters(structureModel: StructureModel, methodModel : MethodModel) : String {
        val result = StringBuilder()
        var del = ""

        for (p in methodModel.getParameters()) {
            del = appendIntendLine(result, p.jniConverter.getAllocateResourceString(structureModel), del)
        }
        return result.toString()
    }


    private fun getFreeParameters(m : MethodModel) : String {
        val result = StringBuilder()
        var del = ""

        for (p in m.getParameters()) {
            del = appendIntendLine(result, p.jniConverter.getFreeResourcesString(), del)
        }
        return result.toString()
    }

    private fun appendIntendLine(result : StringBuilder, line : String, del : String) : String {
        if (line.isNotEmpty()) {
            result.append(del)
            result.append(line)
            if (del.isEmpty()) {
                return "\n" + " ".repeat(12)
            }
        }
        return del
    }

    private fun getGtkCallSignature(structureModel: StructureModel, methodModel : MethodModel, self : Boolean): String {
        val result = StringBuilder()

        var del = ""

        if (self) {
            result.append("(void*) _self")
            del = ", "
        }

        for (parameterModel in methodModel.getParameters()) {
            result.append("${del}${parameterModel.jniConverter.getCallSignatureString(structureModel)}")
            del = ", "
        }

        if (methodModel.throwsError) {
            result.append("${del}NULL")
        }
        return result.toString()
    }


    private fun getReturnStatement(m : MethodModel) : String {
        if (!m.returnType.isVoid) {
            if (m.returnType.isJavaNative) {
                return "return (${m.returnType.jniType})"
            } else {
                return "return (jlong)"
            }
        } else {
            return ""
        }
    }


    override fun writeMallocConstructor(structureModel : StructureModel) {
        out.start(2)
        out.a("""
            JNIEXPORT jlong JNICALL ${getJniMethodName(structureModel, "newFromMalloc")}(JNIEnv * _jenv, jclass _jself)
            {
                return (jlong) calloc(1, sizeof(${structureModel.cType}));
            }
        """,0)
        out.end(2)
    }

    override fun writeCallback(structureModel: StructureModel, methodModel: MethodModel) {
        out.start(2)

        out.a("""
        static ${methodModel.returnType.gtkType} ${getJniSignalCallbackName(structureModel, methodModel)}${getCallbackSignature(methodModel)}
        {
            JavaVM* globalVM    = ${getGlobalVMName(structureModel)};
            jclass  globalClass = ${getGlobalClassName(structureModel)};
            JNIEnv* g_env;
            int     envStat     = (*globalVM)->GetEnv(globalVM, (void **)&g_env, JNI_VERSION_1_6);

            if (envStat == JNI_OK) {
                jmethodID callback = ${getCallbackMethodID(methodModel)};
                ${getCallbackMethodCall(methodModel)};
        
                (*globalVM)->DetachCurrentThread(globalVM);
        
            } else {
                printf("ERROR: JNI is not initialized\\n");
            }
        }
        """.trimIndent())
        out.end(2)
    }

    override fun writeSignal(structureModel : StructureModel, methodModel : MethodModel) {
        out.start(2)

        out.a("""
        static ${methodModel.returnType.gtkType} ${getJniSignalCallbackName(structureModel, methodModel)}${getSignalCallbackSignature(methodModel)}
        {
            JavaVM* globalVM    = ${getGlobalVMName(structureModel)};
            jclass  globalClass = ${getGlobalClassName(structureModel)};
            JNIEnv* g_env;
            int     envStat     = (*globalVM)->GetEnv(globalVM, (void **)&g_env, JNI_VERSION_1_6);

            if (envStat == JNI_OK) {
                jmethodID callback = ${getSignalCallbackMethodID(methodModel)};
                ${getSignalCallbackMethodCall(methodModel)};
        
                (*globalVM)->DetachCurrentThread(globalVM);
        
            } else {
                printf("ERROR: JNI is not initialized\\n");
            }
        }
    
        JNIEXPORT void JNICALL ${getJniSignalConnectMethodName(structureModel, methodModel)}(JNIEnv * _jenv, jclass _jself, jlong _self) 
        {
            printf("JNI connect: ${methodModel.apiName}\n");
            ${getEnvironmentInit(structureModel, methodModel, true)}    
            g_signal_connect ((void *)_self, "${methodModel.name}", G_CALLBACK (${getJniSignalCallbackName(structureModel, methodModel)}), NULL);
        }
        """.trimIndent())
    }


    private fun getEnvironmentInit(structureModel: StructureModel, methodModel: MethodModel, isSignal : Boolean) : String {
        // TODO move isSignal to methodModel (for example to hasCallback())
        return if (isSignal || methodModel.hasCallback()) {
            "(*_jenv)->GetJavaVM(_jenv, &${getGlobalVMName(structureModel)});\n            ${getGlobalClassName(structureModel)} = (jclass) (*_jenv)->NewGlobalRef(_jenv, _jself);"
        } else {
            "";
        }
    }


    override fun writeField(structureModel : StructureModel, parameterModel : ParameterModel) {
        out.start(2)
        val getter = getJavaFieldGetterName(parameterModel.name)
        val setter = getJavaFieldSetterName(parameterModel.name)


        var directAccess = ""
        if (parameterModel.isDirectType) {
            directAccess = "&"
        }

        out.a("""
        JNIEXPORT ${parameterModel.jniType} JNICALL ${getJniMethodName(structureModel, getter)}(JNIEnv * _jenv, jclass _jself, jlong _self)
        {
            const ${structureModel.cType}* __self = (${structureModel.cType}*) _self;
            return (${parameterModel.jniType}) ${directAccess}__self->${parameterModel.name};
        }
        
        """.trimIndent())

        if (parameterModel.isWriteable && !parameterModel.isDirectType)
        out.a("""
        JNIEXPORT void JNICALL ${getJniMethodName(structureModel, setter)}(JNIEnv * _jenv, jclass _jself, jlong _self, ${parameterModel.jniType} _${parameterModel.name})
        {
            ${structureModel.cType}* __self = (${structureModel.cType}*) _self;
            __self->${parameterModel.name} = (${parameterModel.gtkType}) _${parameterModel.name};
        }
        
        """.trimIndent())
        out.end(2)
    }

    override fun writeFunction(structureModel : StructureModel, methodModel : MethodModel) {
        out.start(2)
        _writeNativeMethod(structureModel, methodModel, false)
        out.end(2)
    }

    override fun writeUnsupported(model: Model) {}
    override fun writeEnd() {}

    private fun getCallbackMethodID(methodModel : MethodModel) : String {
        return "(*g_env)->GetStaticMethodID(g_env, globalClass, \"${getImpJavaSignalCallbackName(methodModel.name)}\", \"${getJniIdSignature(methodModel)}\")"
    }

    private fun getSignalCallbackMethodID(methodModel : MethodModel) : String {
        return "(*g_env)->GetStaticMethodID(g_env, globalClass, \"${getImpJavaSignalCallbackName(methodModel.name)}\", \"${getSignalJniIdSignature(methodModel)}\")"
    }


    private fun getJniIdSignature(methodModel : MethodModel) : String {
        val result = StringBuilder()

        result.append("(")
        for (p in methodModel.getParameters()) {
            result.append(p.jniSignatureID)
        }
        result.append(")")
        result.append(methodModel.returnType.jniSignatureID)
        return result.toString()
    }

    private fun getSignalJniIdSignature(methodModel : MethodModel) : String {
        val result = StringBuilder()

        result.append("(J")
        for (p in methodModel.getParameters()) {
            result.append(p.jniSignatureID)
        }
        result.append(")")
        result.append(methodModel.returnType.jniSignatureID)
        return result.toString()
    }


    private fun getCallbackMethodCall(methodModel : MethodModel) : String {
        val result = StringBuilder()

        if (!methodModel.returnType.isVoid) {
            result.append("return (${methodModel.returnType.gtkType})")
        }
        result.append("(*g_env)->${methodModel.returnType.jniCallbackMethodName}(g_env, globalClass, callback")

        for (p in methodModel.getParameters()) {
            result.append(", (${p.jniType})${p.name}")
        }

        result.append(")")
        return result.toString()
    }

    private fun getSignalCallbackMethodCall(methodModel : MethodModel) : String {
        val result = StringBuilder()

        if (!methodModel.returnType.isVoid) {
            result.append("return (${methodModel.returnType.jniType})")
        }
        result.append("(*g_env)->${methodModel.returnType.jniCallbackMethodName}(g_env, globalClass, callback, (jlong)_self")

        for (p in methodModel.getParameters()) {
            result.append(", (${p.jniType})${p.name}")
        }

        result.append(")")
        return result.toString()
    }


    private fun getCallbackSignature(methodModel : MethodModel) : String {
        val result = StringBuilder()

        var del = "";

        result.append("(")
        for (p in methodModel.getParameters()) {
            result.append("${del}${p.gtkType} ${p.name}")
            del = ", "
        }
        result.append(")")
        return result.toString()
    }

    private fun getSignalCallbackSignature(methodModel : MethodModel) : String {
        val result = StringBuilder()

        result.append("(void* _self, ")
        for (p in methodModel.getParameters()) {
            result.append("${p.gtkType} ${p.name}, ")
        }
        result.append("void* _data)")
        return result.toString()
    }

    private fun getGlobalClassName(structureModel : StructureModel) : String {
        return getJniGlobalsName(structureModel,"class")
    }

    private fun getGlobalVMName(structureModel : StructureModel) : String {
        return getJniGlobalsName(structureModel,"VM")
    }



    override fun writeClass(structureModel: StructureModel) {}
    override fun writeInterface(structureModel: StructureModel) {}
    override fun writeInternalConstructor(structureModel: StructureModel) {}
    override fun writeConstructor(structureModel: StructureModel, methodModel: MethodModel) {}
    override fun writeFactory(structureModel: StructureModel, methodModel: MethodModel) {}
}
