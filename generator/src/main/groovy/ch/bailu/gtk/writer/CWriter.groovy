package ch.bailu.gtk.writer

import ch.bailu.gtk.converter.JavaNames
import ch.bailu.gtk.model.ClassModel;
import ch.bailu.gtk.model.MethodModel;
import ch.bailu.gtk.model.Model;
import ch.bailu.gtk.model.NameSpaceModel;
import ch.bailu.gtk.model.ParameterModel;

class CWriter extends CodeWriter {


    CWriter(Writer writer) {
        super(writer);
    }

    @Override
    void writeStart(ClassModel classModel, NameSpaceModel namespace) throws IOException {
        super.writeStart(classModel, namespace)

        a("#include <jni.h>\n\n");
        for (String include : namespace.getIncludes()) {
            a("#include <${include}>\n")
        }

        a("\n#include \"${classModel.getHeaderFileName()}\"\n\n\n");

        a("jclass ${getGlobalClassName(classModel)};\n");
        a("JavaVM* ${getGlobalVMName(classModel)};\n");

        end(3);
    }


    @Override
    void writeClass(ClassModel classModel) {}

    @Override
    void writeInterface(ClassModel classModel)  {}

    @Override
    void writeUnsupported(Model m) throws IOException {}

    @Override
    void writeNativeMethod(ClassModel classModel, MethodModel m) throws IOException {
        _writeNativeMethod(classModel, m, true);
    }

    private String getFreeParameters(MethodModel m) throws IOException {
        StringBuilder result = new StringBuilder()
        result.append('\n')

        for (ParameterModel p : m.getParameters()) {
            result.append("                ")
            result.append(p.getFreeResourcesString())
        }
        result
    }

    private String getAllocateParameters(MethodModel m) throws IOException {
        StringBuilder result = new StringBuilder()
        result.append('\n')

        for (ParameterModel p : m.getParameters()) {
            result.append("                ")
            result.append(p.getAllocateResourceString())
        }
        result
    }

    private String getGtkCallSignature(MethodModel m, boolean self) throws IOException {
        StringBuilder result = new StringBuilder()

        String del = ''

        if (self) {
            result.append("(void*) _self")
            del = ', '
        }
        
        for (ParameterModel p : m.getParameters()) {
            result.append("${del}${p.getCallSignatureString()}")
            del = ', '
        }

        if (m.throwsError()) {
            result.append("${del}NULL")
        }
        result
    }

    String getJniSignature(MethodModel m, boolean self) throws IOException {
        StringBuilder result = new StringBuilder()
        result.append('JNIEnv * _jenv, jclass _jself')

        if (self) {
            result.append(', jlong _self')
        }

        for (ParameterModel p : m.getParameters()) {
            result.append(", ${p.getJniType()} ${p.getName()}")
        }
        result
    }


    @Override
    void writeInternalConstructor(ClassModel c) throws IOException {}

    @Override
    void writeConstructor(ClassModel c, MethodModel m) throws IOException {}

    @Override
    void writeFactory(ClassModel c, MethodModel m) throws IOException {}

    @Override
    void writePrivateFactory(ClassModel c, MethodModel m) throws IOException {
        _writeNativeMethod(c, m, false);
    }

    private void _writeNativeMethod(ClassModel c, MethodModel m, boolean self) throws IOException {
        start(1)
        a """
            JNIEXPORT ${m.getReturnType().getJniType()} JNICALL ${c.getJniMethodName(m)}(${getJniSignature(m, self)})
            {
                ${getAllocateParameters(m)}
                ${getReturnStatement(m)} ${m.getGtkName()}(${getGtkCallSignature(m, self)});
                ${getFreeParameters(m)}
            }
            """.stripIndent(12)
    }


    private String getReturnStatement(MethodModel m) {
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


    @Override
    void writeConstant(ParameterModel p) throws IOException {}

    @Override
    void writeEnd() throws IOException {}

    @Override
    void writeMallocConstructor(ClassModel c) {
        start(1)
        a("""
            JNIEXPORT jlong JNICALL ${c.getJniMethodName("newFromMalloc")}(JNIEnv * _jenv, jclass _jclass)
            {
                return (jlong) calloc(1, sizeof(${c.getCType()}));
            }

        """.stripIndent(12))
    }

    @Override
    void writeSignal(ClassModel c, MethodModel m) throws IOException {
        start(1)

        a("""
            static ${m.getReturnType().gtkType} ${c.getCSignalCallbackName(m)}${getCallBackSignature(m)}
            {
                JavaVM* globalVM    = ${getGlobalVMName(c)};
                jclass  globalClass = ${getGlobalClassName(c)};
                JNIEnv* g_env;
                int     envStat     = (*globalVM)->GetEnv(globalVM, (void **)&g_env, JNI_VERSION_1_6);

                printf(\"JNI received: ${m.getApiName()}\\n\");

                if (envStat == JNI_OK) {
                    jmethodID callback = ${getCallbackMethodID(m)};
                    ${getCallbackMethodCall(m)};
        
                    (*globalVM)->DetachCurrentThread(globalVM);
        
                } else {
                    printf(\"ERROR: JNI is not initialized\\n\");
                }
            }
    
            JNIEXPORT void  JNICALL ${c.getJniSignalConnectMethodName(m)}(JNIEnv * _jenv, jclass _jclass, jlong _self) 
            {
                printf(\"JNI connect: ${m.getApiName()}\\n\");

                (*_jenv)->GetJavaVM(_jenv, &${getGlobalVMName(c)});
                ${getGlobalClassName(c)} = (jclass) (*_jenv)->NewGlobalRef(_jenv, _jclass);
    
                g_signal_connect ((void *)_self, \"${m.getApiName()}\", G_CALLBACK (${c.getCSignalCallbackName(m)}), NULL);
            }
            """.stripIndent(12))
    }

    @Override
    void writeField(ClassModel c, ParameterModel p) {
        String getter = JavaNames.getGetterName(p.getName())
        String setter = JavaNames.getSetterName(p.getName())

        a """
            JNIEXPORT ${p.getJniType()} JNICALL ${c.getJniMethodName(getter)}(JNIEnv * _jenv, jclass _jclass, jlong _self)
            {
                const ${c.getCType()}* __self = (${c.getCType()}*) _self;
                return (${p.getJniType()}) __self->${p.getName()};
            }


            JNIEXPORT void JNICALL ${c.getJniMethodName(setter)}(JNIEnv * _jenv, jclass _jclass, jlong _self, ${p.getJniType()} _${p.getName()})
            {
                ${c.getCType()}* __self = (${c.getCType()}*) _self;
                __self->${p.getName()} = (${p.getGtkType()}) _${p.getName()};
            }
            
            
        """.stripIndent(12)
    }

    @Override
    void writeFunction(ClassModel c, MethodModel m) {
        _writeNativeMethod(c, m, false)
    }

    private String getCallbackMethodID(MethodModel m) {
        "(*g_env)->GetStaticMethodID(g_env, globalClass, \"${m.getSignalCallbackName()}\", \"${getJniIdSignature(m)}\")"
    }

    private String getJniIdSignature(MethodModel m) {
        StringBuilder result = new StringBuilder()

        result.append '(J'
        for (ParameterModel p : m.getParameters()) {
            result.append p.getJniSignatureID()
        }
        result.append ')'
        result.append m.getReturnType().getJniSignatureID()
        result
    }

    private String getCallbackMethodCall(MethodModel m) {
        StringBuilder result = new StringBuilder()

        if (!m.getReturnType().isVoid()) {
            result.append "return (${m.getReturnType().jniType})"
        }
        result.append "(*g_env)->${m.getReturnType().getJniCallbackMethodName()}(g_env, globalClass, callback, (jlong)_self"

        for (ParameterModel p: m.getParameters()) {
            result.append ", (${p.jniType})${p.getName()}"
        }

        result.append ')'
        result

    }


    private String getCallBackSignature(MethodModel m) throws IOException {
        StringBuilder result = new StringBuilder();

        result.append '(void* _self, '
        for (ParameterModel p : m.getParameters()) {
            result.append("${p.getGtkType()} ${p.getName()}, ")
        }
        result.append 'void* _data)'
        result
    }

    private String getGlobalClassName(ClassModel c) {
        return c.getGlobalName("class");
    }

    private String getGlobalVMName(ClassModel c) {
        return c.getGlobalName("VM");
    }
}
