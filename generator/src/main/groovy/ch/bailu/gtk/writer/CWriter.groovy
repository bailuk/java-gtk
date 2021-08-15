package ch.bailu.gtk.writer


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

    private void writeFreeParameters(MethodModel m) throws IOException {
        for (ParameterModel p : m.getParameters()) {
            a(p.getFreeResourcesString());
        }
    }

    private void writeAllocateParameters(MethodModel m) throws IOException {
        for (ParameterModel p : m.getParameters()) {
            a(p.getAllocateResourceString());
        }

    }

    private void writeGtkCallSignature(MethodModel m, boolean self) throws IOException {
        String del = "";

        a("(");

        if (self) {
            a("(void*) _self");
            del = ",";
        }
        
        for (ParameterModel p : m.getParameters()) {
            a(del).a(p.getCallSignatureString());
            del = ",";
        }

        if (m.throwsError()) {
            a(del).a("NULL");
        }
        
        a(")");
    }

    private void writeJniSignature(MethodModel m, boolean self) throws IOException {
        String del = ", ";

        a("(").a("JNIEnv * _jenv, jclass _jself");

        if (self) {
            a(del).a("jlong _self");
        }
        for (ParameterModel p : m.getParameters()) {
            a(del).a(p.getJniType()).a(" ").a(p.getName());
            
        }
        a(")");
    }


    @Override
    public void writeInternalConstructor(ClassModel c) throws IOException {}

    @Override
    public void writeConstructor(ClassModel c, MethodModel m) throws IOException {}

    @Override
    public void writeFactory(ClassModel c, MethodModel m) throws IOException {
    }

    @Override
    public void writePrivateFactory(ClassModel c, MethodModel m) throws IOException {
        _writeNativeMethod(c, m, false);
    }

    private void _writeNativeMethod(ClassModel c, MethodModel m, boolean self) throws IOException {
        start(1);
        a("\nJNIEXPORT ").a(m.getReturnType().getJniType()).a(" JNICALL ").a(c.getJniMethodName(m));
        writeJniSignature(m, self);


        a(" {\n");
        writeAllocateParameters(m);
        a("\n");

        a("    ");
        if (!m.getReturnType().isVoid()) {


            if (m.getReturnType().isJavaNative()) {
                a("return (" + m.getReturnType().getJniType() + ") ");
            } else {
                a("return (jlong) ");
            }


        }
        a(m.getGtkName() + "");
        writeGtkCallSignature(m, self);
        a(";\n");
        writeFreeParameters(m);
        a("}\n");

    }

  

    @Override
    public void writeConstant(ParameterModel p) throws IOException {}

    @Override
    void writeEnd() throws IOException {}

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
""")
    }

    @Override
    void writeField(ClassModel classModel, ParameterModel p) {

    }

    @Override
    void writeFunction(ClassModel classModel, MethodModel m) {

    }

    private String getCallbackMethodID(MethodModel m) {
        "(*g_env)->GetStaticMethodID(g_env, globalClass, \"${m.getSignalCallbackName()}\", \"${getJNISignature(m)}\")"
    }

    private String getJNISignature(MethodModel m) {
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
