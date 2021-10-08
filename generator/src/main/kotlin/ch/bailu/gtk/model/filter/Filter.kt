package ch.bailu.gtk.model.filter

import ch.bailu.gtk.model.ClassModel
import ch.bailu.gtk.model.MethodModel
import ch.bailu.gtk.model.ParameterModel
import ch.bailu.gtk.tag.ParameterTag

fun values(name: String, value: String): Boolean {
    return ("2147483648" != value
            && "9223372036854775807" != value
            && "4294967295" != value
            && "18446744073709551615" != value
            && "-9223372036854775808" != value
            && "86400000000" != value
            && "3600000000" != value)
}

fun method(classModel: ClassModel, methodModel: MethodModel): Boolean {
    if ("MenuItem" == classModel.apiName && "activate" == methodModel.apiName) {
        return false
    }
    if ("ToolPalette" == classModel.apiName && "getStyle" == methodModel.apiName) {
        return false
    }
    if ("Toolbar" == classModel.apiName && "getStyle" == methodModel.apiName) {
        return false
    }
    if ("Coverage" == classModel.apiName && "ref" == methodModel.apiName) {
        return false
    }
    return !("PrintSettings" == classModel.apiName && "get" == methodModel.apiName)
}


fun field(classModel: ClassModel, parameterModel: ParameterModel): Boolean {
    if ("PixbufAnimationIterClass" == classModel.apiName) {
        return false
    }
    if ("SettingsBackendClass" == classModel.apiName) {
        return false
    }
    if ("PixbufFormat" == classModel.apiName) {
        return false
    }
    if ("PixbufModule" == classModel.apiName) {
        return false
    }
    return "PixbufModulePattern" != classModel.apiName
}

/**
 * List of records that support malloc constructor
 */
private val MALLOC = arrayOf(
        "RGBA",
        "Rectangle",
        "Matrix"
)


fun createMalloc(classModel: ClassModel): Boolean {
    for (s in MALLOC) {
        if (s == classModel.apiName) {
            return true
        }
    }
    return false
}

fun fieldDirectAccessAllowed(classModel: ClassModel, field: ParameterTag): Boolean {
    return "AttrShape" == classModel.apiName
}