package ch.bailu.gtk.writer

import ch.bailu.gtk.model.ClassModel
import ch.bailu.gtk.model.MethodModel
import ch.bailu.gtk.model.ParameterModel


/**
 * function name of a c callback function
 * gobject_Closure_onClosureNotify
 */
fun getCSignalCallbackName(classModel : ClassModel, methodModel : MethodModel) : String {
    return getCCallbackName(classModel.nameSpaceModel.namespace, classModel.apiName, methodModel.signalMethodName)
}

fun getCCallbackName(classModel : ClassModel, parameterModel: ParameterModel) : String {
    return getCCallbackName(classModel.nameSpaceModel.namespace, classModel.apiName, parameterModel.callbackModel.signalMethodName)
}

private fun getCCallbackName(namespace: String, className: String, methodName: String) : String {
    return "${namespace}_${className}_${methodName}"
}