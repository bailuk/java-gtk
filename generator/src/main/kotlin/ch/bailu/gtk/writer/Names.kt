package ch.bailu.gtk.writer

import ch.bailu.gtk.Configuration
import ch.bailu.gtk.model.ClassModel
import ch.bailu.gtk.model.MethodModel
import ch.bailu.gtk.model.NamespaceModel
import ch.bailu.gtk.model.ParameterModel
import ch.bailu.gtk.table.ReservedTokenTable.convert


fun getJavaImpClassName(name : String): String {
    return "Imp{$name}"
}


/**
 * function name of a c callback function
 * gobject_Closure_onClosureNotify
 */
fun getJniSignalCallbackName(classModel : ClassModel, methodModel : MethodModel) : String {
    return getJniCallbackName(classModel.nameSpaceModel.namespace, classModel.apiName, methodModel.signalMethodName)
}


fun getJniCallbackName(classModel : ClassModel, parameterModel: ParameterModel) : String {
    return getJniCallbackName(classModel.nameSpaceModel.namespace, classModel.apiName, parameterModel.callbackModel.signalMethodName)
}


private fun getJniCallbackName(namespace: String, className: String, methodName: String) : String {
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


fun getJniGlobalsName(classModel: ClassModel, name: String): String {
    return classModel.nameSpaceModel.namespace + "_" + classModel.impName + "_" + name
}


fun getJniHeaderFileName(classModel: ClassModel) : String {
    return getJniHeaderFileBase(classModel.nameSpaceModel) + classModel.impName + ".h"
}


fun getJniHeaderFileBase(namespaceModel : NamespaceModel): String {
    return Configuration.HEADER_FILE_BASE + namespaceModel.namespace + "_"
}


fun getJavaMethodName(name: String): String {
    if (name.length < 3) {
        return name
    }
    val result = StringBuilder()
    val names = name.split("_".toRegex()).toTypedArray()
    result.append(names[0])
    for (i in 1 until names.size) {
        firstUpper(result, names[i])
    }
    return fixToken(result.toString())
}


fun getJavaClassName(name: String): String {
    val result = StringBuilder()
    val names = name.split("_".toRegex()).toTypedArray()
    for (i in names.indices) {
        firstUpper(result, names[i])
    }
    return fixToken(result.toString())
}


private fun firstUpper(result: StringBuilder, s: String) {
    if (s.isNotEmpty()) {
        result.append(Character.toUpperCase(s[0]))
        if (s.length > 1) {
            result.append(s.substring(1))
        }
    } else {
        result.append(s)
    }
}


fun getJavaSignalName(prefix: String, name: String): String {
    val result = StringBuilder()
    result.append(prefix)
    val names = name.split("-".toRegex()).toTypedArray()
    for (i in names.indices) {
        firstUpper(result, names[i])
    }
    return fixToken(result.toString())
}


fun fixToken(token: String): String {
    return convert(token)
}


fun getJavaFieldSetterName(name: String): String {
    return getJavaMethodName("set_field_$name")
}


fun getJavaFieldGetterName(name: String): String {
    return getJavaMethodName("get_field_$name")
}


fun getJavaPackageConstantsInterfaceName(namespace: String): String {
    return getJavaClassName(namespace) + "Constants"
}
