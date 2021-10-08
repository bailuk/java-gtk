package ch.bailu.gtk.writer

import ch.bailu.gtk.Configuration
import ch.bailu.gtk.model.ClassModel
import ch.bailu.gtk.model.MethodModel
import ch.bailu.gtk.model.NamespaceModel
import ch.bailu.gtk.model.ParameterModel

fun getImpPrefix() : String {
    return "Imp"
}


fun getImpName(name : String): String? {
    return "${getImpPrefix()}{$name}"
}


/**
 * function name of a c callback function
 * gobject_Closure_onClosureNotify
 */
fun getCSignalCallbackName(classModel : ClassModel, methodModel : MethodModel) : String {
    return getCCallbackName(classModel.nameSpaceModel.getNamespace(), classModel.apiName, methodModel.signalMethodName)
}

fun getCCallbackName(classModel : ClassModel, parameterModel: ParameterModel) : String {
    return getCCallbackName(classModel.nameSpaceModel.namespace, classModel.apiName, parameterModel.callbackModel.signalMethodName)
}

private fun getCCallbackName(namespace: String, className: String, methodName: String) : String {
    return "${namespace}_${className}_${methodName}"
}




fun getJniMethodName(classModel: ClassModel, methodModel: MethodModel): String {
    return getJniMethodName(classModel, methodModel.apiName)
}


fun getJniMethodName(classModel: ClassModel, methodName : String): String {
    return Configuration.JNI_METHOD_NAME_BASE + classModel.nameSpaceModel.namespace + "_" + classModel.impName + "_" + methodName
}

fun getJniSignalConnectMethodName(classModel: ClassModel, methodModel: MethodModel): String {
    return Configuration.JNI_METHOD_NAME_BASE +
            classModel.nameSpaceModel.namespace +
            "_" +
            classModel.impName +
            "_" +
            methodModel.signalMethodName
}


fun getGlobalName(classModel: ClassModel, name: String): String {
    return classModel.nameSpaceModel.namespace + "_" + classModel.impName + "_" + name
}




fun getHeaderFileName(classModel: ClassModel) : String {
    return getHeaderFileBase(classModel.nameSpaceModel) + classModel.impName + ".h"
}

fun getHeaderFileBase(namespaceModel : NamespaceModel): String {
    return Configuration.HEADER_FILE_BASE + namespaceModel.namespace + "_"
}
