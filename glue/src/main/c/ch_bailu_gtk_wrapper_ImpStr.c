#include <jni.h>
#include <stdlib.h>
#include <string.h>

#include "ch_bailu_gtk_wrapper_ImpStr.h"


/**
    Copy a Java String to a heap memory region in c-space
*/
JNIEXPORT jlong JNICALL Java_ch_bailu_gtk_wrapper_ImpStr_createStr
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



/**
    Creates and returns a Java String containing the heap memory string.
    Assumes that the c-space string is null terminated.
    The returned Java String is a copy of the c-space string.
*/
JNIEXPORT jstring JNICALL Java_ch_bailu_gtk_wrapper_ImpStr_toString
(JNIEnv * _jenv, jclass _class, jlong _pointer)
{
    const char* src  = (char*) _pointer;
    return (*_jenv)->NewStringUTF(_jenv, src);
}
