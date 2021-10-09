package ch.bailu.gtk.writer

import ch.bailu.gtk.Configuration
import ch.bailu.gtk.model.StructureModel
import ch.bailu.gtk.model.MethodModel
import ch.bailu.gtk.model.NamespaceModel
import ch.bailu.gtk.model.ParameterModel
import ch.bailu.gtk.table.ReservedTokenTable.convert


fun getJavaImpClassName(name : String): String {
    return "Imp${name}"
}


/**
 * function name of a c callback function
 * gobject_Closure_onClosureNotify
 */
fun getJniSignalCallbackName(structureModel : StructureModel, methodModel : MethodModel) : String {
    return getJniCallbackName(structureModel.nameSpaceModel.getNamespace(), structureModel.apiName, getJavaSignalMethodName(methodModel.name))
}


fun getJniCallbackName(structureModel : StructureModel, parameterModel: ParameterModel) : String {
    val cbModel = parameterModel.callbackModel
    val cbName = cbModel?.name ?: ""
    return getJniCallbackName(structureModel.nameSpaceModel.getNamespace(), structureModel.apiName, getJavaSignalMethodName(cbName))
}


private fun getJniCallbackName(namespace: String, className: String, methodName: String) : String {
    return "${namespace}_${className}_${methodName}"
}


fun getJniMethodName(structureModel: StructureModel, methodModel: MethodModel): String {
    return getJniMethodName(structureModel, methodModel.apiName)
}


fun getJniMethodName(structureModel: StructureModel, methodName : String): String {
    return Configuration.JNI_METHOD_NAME_BASE + structureModel.nameSpaceModel.getNamespace() + "_" + structureModel.impName + "_" + methodName
}


fun getJniSignalConnectMethodName(structureModel: StructureModel, methodModel: MethodModel): String {
    return Configuration.JNI_METHOD_NAME_BASE +
            structureModel.nameSpaceModel.getNamespace() +
            "_" +
            structureModel.impName +
            "_" +
            getJavaSignalMethodName(methodModel.name)
}


fun getJniGlobalsName(structureModel: StructureModel, name: String): String {
    return structureModel.nameSpaceModel.getNamespace() + "_" + structureModel.impName + "_" + name
}


fun getJniHeaderFileName(structureModel: StructureModel) : String {
    return getJniHeaderFileBase(structureModel.nameSpaceModel) + structureModel.impName + ".h"
}


fun getJniHeaderFileBase(namespaceModel : NamespaceModel): String {
    return Configuration.HEADER_FILE_BASE + namespaceModel.getNamespace() + "_"
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


fun getJavaSignalMethodName(name: String): String {
    return getJavaSignalName("on", name)
}

fun getJavaSignalInterfaceName(name: String): String {
    return getJavaSignalName("On", name)
}

fun getImpJavaSignalCallbackName(name: String): String {
    return getJavaSignalName("callbackOn", name)
}

private fun getJavaSignalName(prefix: String, name: String): String {
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
