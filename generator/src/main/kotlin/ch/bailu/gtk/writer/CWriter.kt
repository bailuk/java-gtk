package ch.bailu.gtk.writer

import ch.bailu.gtk.model.NamespaceModel
import ch.bailu.gtk.converter.JavaNames
import ch.bailu.gtk.model.ClassModel
import ch.bailu.gtk.model.MethodModel
import ch.bailu.gtk.model.Model
import ch.bailu.gtk.model.ParameterModel
import java.io.Writer


class CWriter (writer : Writer) : CodeWriter(writer) {

    override fun writeStart(classModel : ClassModel, namespaceModel : NamespaceModel) {
        super.writeStart(classModel, namespaceModel)

        a("\n#include <jni.h>\n")

        for (include in namespaceModel.getIncludes()) {
            a("#include <${include}>\n")
        }

        a("#include \"${getHeaderFileName(classModel)}\"\n\n\n")

        a("jclass ${getGlobalClassName(classModel)};\n")
        a("JavaVM* ${getGlobalVMName(classModel)};\n")

        end(3)
    }


    override fun writeNativeMethod(classModel : ClassModel, methodModel : MethodModel) {
        start(2)
        _writeNativeMethod(classModel, methodModel, true)
        next()
    }


    override fun writePrivateFactory(classModel : ClassModel, methodModel : MethodModel) {
        start(2)
        _writeNativeMethod(classModel, methodModel, false);
        next()
    }

    override fun writeConstant(parameterModel: ParameterModel) {
        
    }

    private fun _writeNativeMethod(classModel : ClassModel, methodModel : MethodModel, self : Boolean) {
        a ("""
        JNIEXPORT ${methodModel.getReturnType().getJniType()} JNICALL ${getJniMethodName(classModel, methodModel)}(${getJniSignature(methodModel, self)})
        {
            ${getAllocateParameters(classModel, methodModel)}
            ${getEnvironmentInit(classModel, methodModel, false)}
            ${getReturnStatement(methodModel)} ${methodModel.getGtkName()}(${getGtkCallSignature(classModel, methodModel, self)});
            ${getFreeParameters(methodModel)}
        }
        """.trimIndent())
    }


    fun getJniSignature(methodModel : MethodModel, self : Boolean) : String {
        val result = StringBuilder()
        result.append("JNIEnv * _jenv, jclass _jself")

        if (self) {
            result.append(", jlong _self")
        }

        for (p in methodModel.getParameters()) {
            if (!p.isCallback) {
                result.append(", ${p.getJniType()} ${p.getName()}")
            }
        }
        return result.toString()
    }


    private fun getAllocateParameters(classModel: ClassModel, methodModel : MethodModel) : String {
        val result = StringBuilder()
        var del = ""

        for (p in methodModel.getParameters()) {
            del = appendIntendLine(result, p.jniConverter.getAllocateResourceString(classModel), del)
        }
        return result.toString()
    }


    private fun getFreeParameters(m : MethodModel) : String {
        val result = StringBuilder()
        var del = ""

        for (p in m.getParameters()) {
            del = appendIntendLine(result, p.jniConverter.freeResourcesString, del)
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

    private fun getGtkCallSignature(classModel: ClassModel, methodModel : MethodModel, self : Boolean): String {
        val result = StringBuilder()

        var del = ""

        if (self) {
            result.append("(void*) _self")
            del = ", "
        }

        for (parameterModel in methodModel.getParameters()) {
            result.append("${del}${parameterModel.jniConverter.getCallSignatureString(classModel)}")
            del = ", "
        }

        if (methodModel.throwsError()) {
            result.append("${del}NULL")
        }
        return result.toString()
    }


    private fun getReturnStatement(m : MethodModel) : String {
        if (!m.getReturnType().isVoid()) {
            if (m.getReturnType().isJavaNative()) {
                return "return (${m.getReturnType().getJniType()})"
            } else {
                return "return (jlong)"
            }
        } else {
            return ""
        }
    }


    override fun writeMallocConstructor(classModel : ClassModel) {
        start(1)
        a("""
        JNIEXPORT jlong JNICALL ${getJniMethodName(classModel, "newFromMalloc")}(JNIEnv * _jenv, jclass _jself)
        {
            return (jlong) calloc(1, sizeof(${classModel.getCType()}));
        }
        """.trimIndent())
        next()
    }

    override fun writeInterfaceMethod(classModel: ClassModel, m: MethodModel) {}
    override fun writeCallback(classModel: ClassModel, methodModel: MethodModel) {
        start(1)

        a("""
        static ${methodModel.getReturnType().gtkType} ${getCSignalCallbackName(classModel, methodModel)}${getCallbackSignature(methodModel)}
        {
            JavaVM* globalVM    = ${getGlobalVMName(classModel)};
            jclass  globalClass = ${getGlobalClassName(classModel)};
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
        next()
    }

    override fun writeSignal(classModel : ClassModel, methodModel : MethodModel) {
        start(1)

        a("""
        static ${methodModel.getReturnType().gtkType} ${getCSignalCallbackName(classModel, methodModel)}${getSignalCallbackSignature(methodModel)}
        {
            JavaVM* globalVM    = ${getGlobalVMName(classModel)};
            jclass  globalClass = ${getGlobalClassName(classModel)};
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
    
        JNIEXPORT void JNICALL ${getJniSignalConnectMethodName(classModel, methodModel)}(JNIEnv * _jenv, jclass _jself, jlong _self) 
        {
            printf("JNI connect: ${methodModel.getApiName()}\n");
            ${getEnvironmentInit(classModel, methodModel, true)}    
            g_signal_connect ((void *)_self, "${methodModel.getApiName()}", G_CALLBACK (${getCSignalCallbackName(classModel, methodModel)}), NULL);
        }
        """.trimIndent())
    }


    private fun getEnvironmentInit(classModel: ClassModel, methodModel: MethodModel, isSignal : Boolean) : String {
        // TODO move isSignal to methodModel (for example to hasCallback())
        return if (isSignal || methodModel.hasCallback()) {
            "(*_jenv)->GetJavaVM(_jenv, &${getGlobalVMName(classModel)});\n            ${getGlobalClassName(classModel)} = (jclass) (*_jenv)->NewGlobalRef(_jenv, _jself);"
        } else {
            "";
        }
    }


    override fun writeField(classModel : ClassModel, parameterModel : ParameterModel) {
        start(2)
        val getter = JavaNames.getGetterName(parameterModel.getName())
        val setter = JavaNames.getSetterName(parameterModel.getName())


        var directAccess = ""
        if (parameterModel.isDirectType) {
            directAccess = "&"
        }

        a ("""
        JNIEXPORT ${parameterModel.getJniType()} JNICALL ${getJniMethodName(classModel, getter)}(JNIEnv * _jenv, jclass _jself, jlong _self)
        {
            const ${classModel.getCType()}* __self = (${classModel.getCType()}*) _self;
            return (${parameterModel.getJniType()}) ${directAccess}__self->${parameterModel.getName()};
        }
        
        """.trimIndent())

        if (parameterModel.isWriteable && !parameterModel.isDirectType)
        a ("""
        JNIEXPORT void JNICALL ${getJniMethodName(classModel, setter)}(JNIEnv * _jenv, jclass _jself, jlong _self, ${parameterModel.getJniType()} _${parameterModel.getName()})
        {
            ${classModel.getCType()}* __self = (${classModel.getCType()}*) _self;
            __self->${parameterModel.getName()} = (${parameterModel.getGtkType()}) _${parameterModel.getName()};
        }
        
        """.trimIndent())
        next()
    }

    override fun writeFunction(classModel : ClassModel, methodModel : MethodModel) {
        start(2)
        _writeNativeMethod(classModel, methodModel, false)
        next()
    }

    override fun writeUnsupported(model: Model) {}
    override fun writeEnd() {}

    private fun getCallbackMethodID(methodModel : MethodModel) : String {
        return "(*g_env)->GetStaticMethodID(g_env, globalClass, \"${methodModel.getSignalCallbackName()}\", \"${getJniIdSignature(methodModel)}\")"
    }

    private fun getSignalCallbackMethodID(methodModel : MethodModel) : String {
        return "(*g_env)->GetStaticMethodID(g_env, globalClass, \"${methodModel.getSignalCallbackName()}\", \"${getSignalJniIdSignature(methodModel)}\")"
    }


    private fun getJniIdSignature(methodModel : MethodModel) : String {
        val result = StringBuilder()

        result.append("(")
        for (p in methodModel.getParameters()) {
            result.append(p.getJniSignatureID())
        }
        result.append(")")
        result.append(methodModel.getReturnType().getJniSignatureID())
        return result.toString()
    }

    private fun getSignalJniIdSignature(methodModel : MethodModel) : String {
        val result = StringBuilder()

        result.append("(J")
        for (p in methodModel.getParameters()) {
            result.append(p.getJniSignatureID())
        }
        result.append(")")
        result.append(methodModel.getReturnType().getJniSignatureID())
        return result.toString()
    }


    private fun getCallbackMethodCall(methodModel : MethodModel) : String {
        val result = StringBuilder()

        if (!methodModel.getReturnType().isVoid()) {
            result.append("return (${methodModel.getReturnType().gtkType})")
        }
        result.append("(*g_env)->${methodModel.getReturnType().getJniCallbackMethodName()}(g_env, globalClass, callback")

        for (p in methodModel.getParameters()) {
            result.append(", (${p.jniType})${p.getName()}")
        }

        result.append(")")
        return result.toString()
    }

    private fun getSignalCallbackMethodCall(methodModel : MethodModel) : String {
        val result = StringBuilder()

        if (!methodModel.getReturnType().isVoid()) {
            result.append("return (${methodModel.getReturnType().jniType})")
        }
        result.append("(*g_env)->${methodModel.getReturnType().getJniCallbackMethodName()}(g_env, globalClass, callback, (jlong)_self")

        for (p in methodModel.getParameters()) {
            result.append(", (${p.jniType})${p.getName()}")
        }

        result.append(")")
        return result.toString()
    }


    private fun getCallbackSignature(methodModel : MethodModel) : String {
        val result = StringBuilder()

        var del = "";

        result.append("(")
        for (p in methodModel.getParameters()) {
            result.append("${del}${p.getGtkType()} ${p.getName()}")
            del = ", "
        }
        result.append(")")
        return result.toString()
    }

    private fun getSignalCallbackSignature(methodModel : MethodModel) : String {
        val result = StringBuilder()

        result.append("(void* _self, ")
        for (p in methodModel.getParameters()) {
            result.append("${p.getGtkType()} ${p.getName()}, ")
        }
        result.append("void* _data)")
        return result.toString()
    }

    private fun getGlobalClassName(classModel : ClassModel) : String {
        return getGlobalName(classModel,"class")
    }

    private fun getGlobalVMName(classModel : ClassModel) : String {
        return getGlobalName(classModel,"VM")
    }



    override fun writeClass(classModel: ClassModel) {}
    override fun writeInterface(classModel: ClassModel) {}
    override fun writeInternalConstructor(classModel: ClassModel) {}
    override fun writeConstructor(classModel: ClassModel, methodModel: MethodModel) {}
    override fun writeFactory(classModel: ClassModel, methodModel: MethodModel) {}
}
