package ch.bailu.gtk.writer

import ch.bailu.gtk.converter.JavaNames
import ch.bailu.gtk.model.*
import java.io.Writer


class CWriter (writer : Writer) : CodeWriter(writer) {
    override fun writeStart(classModel : ClassModel, namespaceModel : NamespaceModel) {
        super.writeStart(classModel, namespaceModel)

        a("#include <jni.h>\n\n")

        for (include in namespaceModel.includes) {
            a("#include <${include}>\n")
        }

        a("\n#include \"${classModel.getHeaderFileName()}\"\n\n\n")

        a("jclass ${getGlobalClassName(classModel)};\n")
        a("JavaVM* ${getGlobalVMName(classModel)};\n")

        end(3)
    }

    override fun writeClass(classModel: ClassModel) {}
    override fun writeInterface(classModel: ClassModel) {}
    override fun writeInternalConstructor(classModel: ClassModel) {}
    override fun writeConstructor(classModel: ClassModel, methodModel: MethodModel) {}
    override fun writeFactory(classModel: ClassModel, methodModel: MethodModel) {}

    override fun writeNativeMethod(classModel : ClassModel, methodModel : MethodModel) {
        _writeNativeMethod(classModel, methodModel, true)
    }

    private fun getFreeParameters(m : MethodModel) : String {
        val result = StringBuilder()

        for (p in m.getParameters()) {
            result.append(p.getFreeResourcesString())
        }
        return result.toString()
    }

    private fun getAllocateParameters(m : MethodModel) : String {
        val result = StringBuilder()

        for (p in m.getParameters()) {
            result.append(p.getAllocateResourceString())
        }
        return result.toString()
    }

    private fun getGtkCallSignature(classModel: ClassModel, methodModel : MethodModel, self : Boolean): String {
        val result = StringBuilder()

        var del = ""

        if (self) {
            result.append("(void*) _self")
            del = ", "
        }
        
        for (p in methodModel.getParameters()) {
            if (p.isCallback) {
                result.append("${del}${classModel.getCSignalCallbackName(p.callbackModel)}")
            } else {
                result.append("${del}${p.getCallSignatureString()}")
            }
            del = ", "
        }

        if (methodModel.throwsError()) {
            result.append("${del}NULL")
        }
        return result.toString()
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


    override fun writePrivateFactory(classModel : ClassModel, methodModel : MethodModel) {
        _writeNativeMethod(classModel, methodModel, false);
    }

    override fun writeConstant(parameterModel: ParameterModel) {
        
    }

    private fun _writeNativeMethod(classModel : ClassModel, methodModel : MethodModel, self : Boolean) {
        start(1)
        a ("""
JNIEXPORT ${methodModel.getReturnType().getJniType()} JNICALL ${classModel.getJniMethodName(methodModel)}(${getJniSignature(methodModel, self)})
{
    ${getAllocateParameters(methodModel)}
    ${getReturnStatement(methodModel)} ${methodModel.getGtkName()}(${getGtkCallSignature(classModel, methodModel, self)});
    ${getFreeParameters(methodModel)}
}
""")
    }


    private fun getReturnStatement(m : MethodModel) : String {
        if (!m.getReturnType().isVoid()) {
            if (m.getReturnType().isJavaNative()) {
                return "return (${m.getReturnType().getJniType()}) "
            } else {
                return "return (jlong) "
            }
        } else {
            return ""
        }
    }


    override fun writeMallocConstructor(classModel : ClassModel) {
        start(1)
        a("""
JNIEXPORT jlong JNICALL ${classModel.getJniMethodName("newFromMalloc")}(JNIEnv * _jenv, jclass _jclass)
{
    return (jlong) calloc(1, sizeof(${classModel.getCType()}));
}
        """)
    }

    override fun writeInterfaceMethod(classModel: ClassModel, m: MethodModel) {}
    override fun writeCallback(classModel: ClassModel, methodModel: MethodModel) {
        start(1)

        a("""
static ${methodModel.getReturnType().gtkType} ${classModel.getCSignalCallbackName(methodModel)}${getCallbackSignature(methodModel)}
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
    """)


    }

    override fun writeSignal(classModel : ClassModel, methodModel : MethodModel) {
        start(1)

        a("""
static ${methodModel.getReturnType().gtkType} ${classModel.getCSignalCallbackName(methodModel)}${getSignalCallbackSignature(methodModel)}
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
    
JNIEXPORT void JNICALL ${classModel.getJniSignalConnectMethodName(methodModel)}(JNIEnv * _jenv, jclass _jclass, jlong _self) 
{
    printf("JNI connect: ${methodModel.getApiName()}\n");

    (*_jenv)->GetJavaVM(_jenv, &${getGlobalVMName(classModel)});
    ${getGlobalClassName(classModel)} = (jclass) (*_jenv)->NewGlobalRef(_jenv, _jclass);
    
    g_signal_connect ((void *)_self, "${methodModel.getApiName()}", G_CALLBACK (${classModel.getCSignalCallbackName(methodModel)}), NULL);
}
    """)
    }




    override fun writeField(classModel : ClassModel, parameterModel : ParameterModel) {
        val getter = JavaNames.getGetterName(parameterModel.getName())
        val setter = JavaNames.getSetterName(parameterModel.getName())

        a ("""
JNIEXPORT ${parameterModel.getJniType()} JNICALL ${classModel.getJniMethodName(getter)}(JNIEnv * _jenv, jclass _jclass, jlong _self)
{
    const ${classModel.getCType()}* __self = (${classModel.getCType()}*) _self;
    return (${parameterModel.getJniType()}) __self->${parameterModel.getName()};
}

JNIEXPORT void JNICALL ${classModel.getJniMethodName(setter)}(JNIEnv * _jenv, jclass _jclass, jlong _self, ${parameterModel.getJniType()} _${parameterModel.getName()})
{
    ${classModel.getCType()}* __self = (${classModel.getCType()}*) _self;
    __self->${parameterModel.getName()} = (${parameterModel.getGtkType()}) _${parameterModel.getName()};
}
    """)
    }

    override fun writeFunction(classModel : ClassModel, methodModel : MethodModel) {
        _writeNativeMethod(classModel, methodModel, false)
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
            result.append("return (${methodModel.getReturnType().jniType})")
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
        return classModel.getGlobalName("class")
    }

    private fun getGlobalVMName(classModel : ClassModel) : String {
        return classModel.getGlobalName("VM")
    }
}
