
#include <jni.h>

#include <gtk/gtk.h>

#include "ch_bailu_gtk_Signal.h"


jclass globalClass;
JavaVM* globalVM;

static void on_activate (void* __self, void* data) {

    printf("JNI received: %s\n", (char*) data);

    JNIEnv * g_env;
     // double check it's all ok
    int getEnvStat = (*globalVM)->GetEnv(globalVM, (void **)&g_env, JNI_VERSION_1_6);
    if (getEnvStat == JNI_EDETACHED) {
        printf("JNI not attached\n");
        if ((*globalVM)->AttachCurrentThread(globalVM, (void **) &g_env, NULL) != 0) {
            printf("JNI failed to attach\n");
        }
    } else if (getEnvStat == JNI_OK) {
        jmethodID callback = (*g_env)->GetStaticMethodID(g_env, globalClass, "callback", "(Ljava/lang/String;)V");
        jstring signame = (*g_env)->NewStringUTF(g_env, (char*)data);
        (*g_env)->CallStaticVoidMethod(g_env, globalClass, callback, signame);
        (*globalVM)->DetachCurrentThread(globalVM);

    } else if (getEnvStat == JNI_EVERSION) {
        printf("JNI version not supported");
    }




}


JNIEXPORT void JNICALL Java_ch_bailu_gtk_Signal_doConnect (JNIEnv * _jenv, jclass _jclass, jlong _self, jstring _signal) {
    const void* __self = (void *) _self;
    const char *__signal = (*_jenv)->GetStringUTFChars(_jenv, _signal, NULL);

    (*_jenv)->GetJavaVM(_jenv, &globalVM);

    globalClass = (jclass) (*_jenv)->NewGlobalRef(_jenv, _jclass);

    printf("JNI connect: %s\n", __signal);

    g_signal_connect (__self, __signal, G_CALLBACK (on_activate), (void*)__signal);


     // Step 2: Perform its intended operations

     // (*env)->ReleaseStringUTFChars(env, inJNIStr, inCStr);  // release resources

}
