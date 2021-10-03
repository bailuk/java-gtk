#include <jni.h>
#include <stdlib.h>
#include <string.h>

#include "ch_bailu_gtk_type_ImpBytes.h"


JNIEXPORT jlong JNICALL Java_ch_bailu_gtk_type_ImpBytes_createBytes
(JNIEnv * _env, jclass _class, jbyteArray _bytes)
{
    jbyte* src  = (*_env)->GetByteArrayElements(_env, _bytes, NULL);
    const jsize  size = (*_env)->GetArrayLength(_env, _bytes);

    void* dest = malloc(size);

    if (dest != NULL) {
        dest = memcpy(dest, src, size);
    }

    (*_env)->ReleaseByteArrayElements(_env, _bytes, src, JNI_ABORT);

    return (jlong) dest;

}

JNIEXPORT jbyte JNICALL Java_ch_bailu_gtk_type_ImpBytes_getByte
  (JNIEnv * _env, jclass _class, jlong _pointer, jint _index)
{
    const void* __pointer = (void*) _pointer;
    const char*  bytes = (char*) __pointer;

    return (jbyte) bytes[_index];
}

