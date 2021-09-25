#include <jni.h>
#include <stdlib.h>
#include <string.h>

#include "ch_bailu_gtk_wrapper_UtilImp.h"


/**
    Allocate and initialize a c-space pointer array
    and return its address back to Java-space
*/
JNIEXPORT jlong JNICALL Java_ch_bailu_gtk_wrapper_UtilImp_createPointerArray
  (JNIEnv * _jenv, jclass _class, jlongArray _array)
{
     const jlong* src = (*_jenv)->GetLongArrayElements(_jenv, _array, NULL);
     const jsize size = (*_jenv)->GetArrayLength(_jenv, _array) * sizeof(jlong);
     const jlong* dest = malloc(size);

     if (dest != NULL) {
        memcpy(dest, src, size);
    }
    (*_jenv)->ReleaseLongArrayElements(_jenv, _array, src, 0);

    return (jlong) dest;
}



/**
    Free c-space heap array
    Usually this frees a c-space copy of Java-space data
*/
JNIEXPORT void JNICALL Java_ch_bailu_gtk_wrapper_UtilImp_destroy
  (JNIEnv * _jenv, jclass _class, jlong _pointer)
{
    free((void*) _pointer);
}


