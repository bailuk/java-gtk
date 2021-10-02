#include <jni.h>
#include <stdlib.h>
#include <string.h>

#include "ch_bailu_gtk_wrapper_ImpDbls.h"




JNIEXPORT jlong JNICALL Java_ch_bailu_gtk_wrapper_ImpDbls_createDoubleArray
  (JNIEnv * _env, jclass _class, jdoubleArray _doubles)
{
    jdouble* src  = (*_env)->GetDoubleArrayElements(_env, _doubles, NULL);
    const jsize  size = (*_env)->GetArrayLength(_env, _doubles) * sizeof(jdouble);
    void* dest = malloc(size);

    if (dest != NULL) {
        dest = memcpy(dest, src, size);
    }

    (*_env)->ReleaseDoubleArrayElements(_env, _doubles, src, JNI_ABORT);

    return (jlong) dest;

}


JNIEXPORT jlong JNICALL Java_ch_bailu_gtk_wrapper_ImpDbls_createDoubleArrayFromFloats
  (JNIEnv * _env, jclass _class, jfloatArray _floats)
{
    jfloat* src  = (*_env)->GetFloatArrayElements(_env, _floats, NULL);
    const jsize length = (*_env)->GetArrayLength(_env, _floats);
    const jsize  size = length * sizeof(jdouble);

    jdouble* dest = malloc(size);

    if (dest != NULL) {
        for (int i = 0; i<length; i++) {
            dest[i] = src[i];
        }
    }

    (*_env)->ReleaseFloatArrayElements(_env, _floats, src, JNI_ABORT);

    return (jlong) dest;


}

JNIEXPORT void JNICALL Java_ch_bailu_gtk_wrapper_ImpDbls_setAt
  (JNIEnv* _env, jclass _class, jlong _pointer, jint _index, jdouble _value)
{
    jdouble* src  = (jdouble*) _pointer;
    src[_index] = _value;
}



JNIEXPORT jdouble JNICALL Java_ch_bailu_gtk_wrapper_ImpDbls_getAt
  (JNIEnv* _env, jclass _class, jlong _pointer, jint _index)
{
    const jdouble* src  = (jdouble*) _pointer;
    return src[_index];
}


JNIEXPORT jlong JNICALL Java_ch_bailu_gtk_wrapper_ImpDbls_createDbl
(JNIEnv * _jenv, jclass _class, jdouble _value)
{
    const jdouble* src  = &_value;
    const jsize  size = sizeof(jdouble);

    void* dest = malloc(size);

    if (dest != NULL) {
        dest = memcpy(dest, src, size);
    }

    return (jlong) dest;
}

