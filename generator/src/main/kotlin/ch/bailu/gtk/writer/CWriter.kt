package ch.bailu.gtk.writer

import ch.bailu.gtk.converter.JavaNames
import ch.bailu.gtk.model.*
import java.io.Writer


class CWriter (writer : Writer) : CodeWriter(writer) {
    override fun writeStart(classModel : ClassModel, namespace : NameSpaceModel) {
        super.writeStart(classModel, namespace)

        a("#include <jni.h>\n\n")

        for (include in namespace.includes) {
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

    fun getFreeParameters(m : MethodModel) : String {
        val result = StringBuilder()
        result.append("\n")

        for (p in m.getParameters()) {
            result.append("                ")
            result.append(p.getFreeResourcesString())
        }
        return result.toString()
    }

    private fun getAllocateParameters(m : MethodModel) : String {
        val result = StringBuilder()
        result.append("\n")

        for (p in m.getParameters()) {
            result.append("                ")
            result.append(p.getAllocateResourceString())
        }
        return result.toString()
    }

    private fun getGtkCallSignature(m : MethodModel, self : Boolean): String {
        val result = StringBuilder()

        var del = ""

        if (self) {
            result.append("(fun*) _self")
            del = ", "
        }
        
        for (p in m.getParameters()) {
            result.append("${del}${p.getCallSignatureString()}")
            del = ", "
        }

        if (m.throwsError()) {
            result.append("${del}NULL")
        }
        return result.toString()
    }

    fun getJniSignature(m : MethodModel, self : Boolean) : String {
        val result = StringBuilder()
        result.append("JNIEnv * _jenv, jclass _jself")

        if (self) {
            result.append(", jlong _self")
        }

        for (p in m.getParameters()) {
            result.append(", ${p.getJniType()} ${p.getName()}")
        }
        return result.toString()
    }


    override fun writePrivateFactory(c : ClassModel, m : MethodModel) {
        _writeNativeMethod(c, m, false);
    }

    override fun writeConstant(parameterModel: ParameterModel) {
        
    }

    private fun _writeNativeMethod(c : ClassModel, m : MethodModel, self : Boolean) {
        start(1)
        a ("""
            JNIEXPORT ${m.getReturnType().getJniType()} JNICALL ${c.getJniMethodName(m)}(${getJniSignature(m, self)})
            {
                ${getAllocateParameters(m)}
                ${getReturnStatement(m)} ${m.getGtkName()}(${getGtkCallSignature(m, self)});
                ${getFreeParameters(m)}
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


    override fun writeMallocConstructor(c : ClassModel) {
        start(1)
        a("""
            JNIEXPORT jlong JNICALL ${c.getJniMethodName("newFromMalloc")}(JNIEnv * _jenv, jclass _jclass)
            {
                return (jlong) calloc(1, sizeof(${c.getCType()}));
            }

        """)
    }

    override fun writeSignal(c : ClassModel, m : MethodModel) {
        start(1)

        a("""
            static ${m.getReturnType().gtkType} ${c.getCSignalCallbackName(m)}${getCallBackSignature(m)}
            {
                JavaVM* globalVM    = ${getGlobalVMName(c)};
                jclass  globalClass = ${getGlobalClassName(c)};
                JNIEnv* g_env;
                int     envStat     = (*globalVM)->GetEnv(globalVM, (fun **)&g_env, JNI_VERSION_1_6);

                printf(\"JNI received: ${m.getApiName()}\\n\");

                if (envStat == JNI_OK) {
                    jmethodID callback = ${getCallbackMethodID(m)};
                    ${getCallbackMethodCall(m)};
        
                    (*globalVM)->DetachCurrentThread(globalVM);
        
                } else {
                    printf(\"ERROR: JNI is not initialized\\n\");
                }
            }
    
            JNIEXPORT fun  JNICALL ${c.getJniSignalConnectMethodName(m)}(JNIEnv * _jenv, jclass _jclass, jlong _self) 
            {
                printf(\"JNI connect: ${m.getApiName()}\\n\");

                (*_jenv)->GetJavaVM(_jenv, &${getGlobalVMName(c)});
                ${getGlobalClassName(c)} = (jclass) (*_jenv)->NewGlobalRef(_jenv, _jclass);
    
                g_signal_connect ((fun *)_self, \"${m.getApiName()}\", G_CALLBACK (${c.getCSignalCallbackName(m)}), NULL);
            }
            """)
    }

    override fun writeField(c : ClassModel, p : ParameterModel) {
        val getter = JavaNames.getGetterName(p.getName())
        val setter = JavaNames.getSetterName(p.getName())

        a ("""
            JNIEXPORT ${p.getJniType()} JNICALL ${c.getJniMethodName(getter)}(JNIEnv * _jenv, jclass _jclass, jlong _self)
            {
                const ${c.getCType()}* __self = (${c.getCType()}*) _self;
                return (${p.getJniType()}) __self->${p.getName()};
            }


            JNIEXPORT fun JNICALL ${c.getJniMethodName(setter)}(JNIEnv * _jenv, jclass _jclass, jlong _self, ${p.getJniType()} _${p.getName()})
            {
                ${c.getCType()}* __self = (${c.getCType()}*) _self;
                __self->${p.getName()} = (${p.getGtkType()}) _${p.getName()};
            }
            
            
        """)
    }

    override fun writeFunction(c : ClassModel, m : MethodModel) {
        _writeNativeMethod(c, m, false)
    }

    override fun writeUnsupported(model: Model) {
        
    }

    override fun writeEnd() {
        
    }

    private fun getCallbackMethodID(m : MethodModel) : String {
        return "(*g_env)->GetStaticMethodID(g_env, globalClass, \"${m.getSignalCallbackName()}\", \"${getJniIdSignature(m)}\")"
    }

    private fun getJniIdSignature(m : MethodModel) : String {
        val result = StringBuilder()

        result.append("(J")
        for (p in m.getParameters()) {
            result.append(p.getJniSignatureID())
        }
        result.append(")")
        result.append(m.getReturnType().getJniSignatureID())
        return result.toString()
    }

    private fun getCallbackMethodCall(m : MethodModel) : String {
        val result = StringBuilder()

        if (!m.getReturnType().isVoid()) {
            result.append("return (${m.getReturnType().jniType})")
        }
        result.append("(*g_env)->${m.getReturnType().getJniCallbackMethodName()}(g_env, globalClass, callback, (jlong)_self")

        for (p in m.getParameters()) {
            result.append(", (${p.jniType})${p.getName()}")
        }

        result.append(")")
        return result.toString()
    }


    private fun getCallBackSignature(m : MethodModel) : String {
        val result = StringBuilder()

        result.append("(fun* _self, ")
        for (p in m.getParameters()) {
            result.append("${p.getGtkType()} ${p.getName()}, ")
        }
        result.append("fun* _data)")
        return result.toString()
    }

    private fun getGlobalClassName(c : ClassModel) : String {
        return c.getGlobalName("class")
    }

    private fun getGlobalVMName(c : ClassModel) : String {
        return c.getGlobalName("VM")
    }
}
