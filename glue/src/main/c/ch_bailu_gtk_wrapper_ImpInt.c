#include <jni.h>
#include <stdlib.h>
#include <string.h>

#include "ch_bailu_gtk_type_ImpInt.h"


/**
    Copy a Java String to a heap memory region in c-space
*/
JNIEXPORT jlong JNICALL Java_ch_bailu_gtk_type_ImpInt_createInt
(JNIEnv * _jenv, jclass _class, jint _value)
{
    const int* src  = &_value;
    const jsize  size = sizeof(jint);

    void* dest = malloc(size);

    if (dest != NULL) {
        dest = memcpy(dest, src, size);
    }

    return (jlong) dest;
}


JNIEXPORT jint JNICALL Java_ch_bailu_gtk_type_ImpInt_get
(JNIEnv * _jenv, jclass _class, jlong _pointer)
{
    const jint* src  = (jint*) _pointer;
    return *src;
}


JNIEXPORT void JNICALL Java_ch_bailu_gtk_type_ImpInt_set
(JNIEnv * _jenv, jclass _class, jlong _pointer, int _value)
{
    jint* src  = (jint*) _pointer;
    *src = _value;
}
