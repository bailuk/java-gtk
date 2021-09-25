#include <jni.h>
#include <stdlib.h>
#include <string.h>

#include "ch_bailu_gtk_wrapper_StrImp.h"


/**
    Copy a Java String to a heap memory region in c-space
*/
JNIEXPORT jlong JNICALL Java_ch_bailu_gtk_wrapper_StrImp_createStr
(JNIEnv * _jenv, jclass _class, jstring _str)
{
    const char* src  = (*_jenv)->GetStringUTFChars(_jenv, _str, NULL);
    const jsize  size = (*_jenv)->GetStringUTFLength(_jenv, _str) + 1;

    void* dest = malloc(size);

    if (dest != NULL) {
        dest = memcpy(dest, src, size);
    }

    (*_jenv)->ReleaseStringUTFChars(_jenv, _str, src);

    return (jlong) dest;

}

