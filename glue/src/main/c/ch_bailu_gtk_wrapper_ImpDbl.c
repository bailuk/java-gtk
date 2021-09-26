#include <jni.h>
#include <stdlib.h>
#include <string.h>

#include "ch_bailu_gtk_wrapper_ImpDbl.h"


/**
    Copy a Java String to a heap memory region in c-space
*/
JNIEXPORT jlong JNICALL Java_ch_bailu_gtk_wrapper_ImpDbl_createDbl
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


JNIEXPORT jdouble JNICALL Java_ch_bailu_gtk_wrapper_ImpDbl_get
(JNIEnv * _jenv, jclass _class, jlong _pointer)
{
    const jdouble* src  = (jdouble*) _pointer;
    return *src;
}


JNIEXPORT void JNICALL Java_ch_bailu_gtk_wrapper_ImpDbl_set
(JNIEnv * _jenv, jclass _class, jlong _pointer, jdouble _value)
{
    jdouble* src  = (jdouble*) _pointer;
    *src = _value;
}
