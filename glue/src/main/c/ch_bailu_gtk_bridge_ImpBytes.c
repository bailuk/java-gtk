
#include "ch_bailu_gtk_bridge_ImpBytes.h"

#include <glib.h>


JNIEXPORT jlong JNICALL Java_ch_bailu_gtk_bridge_ImpBytes_createFromArray
(JNIEnv * _env, jclass _class, jbyteArray _bytes)
{
    const jbyte* src  = (*_env)->GetByteArrayElements(_env, _bytes, NULL);
    const jsize  size = (*_env)->GetArrayLength(_env, _bytes);

    const GBytes* result = g_bytes_new((gconstpointer) src, (gsize) size);

    (*_env)->ReleaseByteArrayElements(_env, _bytes, src, JNI_ABORT);

    return (jlong) result;

}

JNIEXPORT jlong JNICALL Java_ch_bailu_gtk_bridge_ImpBytes_createFromWrapper
  (JNIEnv * _env, jclass _class, jlong _pointer, jint _size)
{
    const jbyte* src  = (jbyte*) _pointer;
    const jsize  size = (jsize) _size;

    return (jlong) g_bytes_new((gconstpointer) src, (gsize) size);
}

