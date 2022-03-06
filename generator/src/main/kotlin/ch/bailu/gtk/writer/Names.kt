package ch.bailu.gtk.writer

import ch.bailu.gtk.model.ParameterModel
import ch.bailu.gtk.model.StructureModel
import ch.bailu.gtk.table.ReservedTokenTable.convert


fun getJniCallbackName(structureModel : StructureModel, parameterModel: ParameterModel) : String {
    val cbModel = parameterModel.callbackModel
    val cbName = cbModel?.name ?: ""
    return getJniCallbackName(structureModel.nameSpaceModel.namespace, structureModel.apiName, getJavaSignalMethodName(cbName))
}


private fun getJniCallbackName(namespace: String, className: String, methodName: String) : String {
    return "${namespace}_${className}_${methodName}"
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
