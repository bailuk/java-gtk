package ch.bailu.gtk.model.filter

import ch.bailu.gtk.model.MethodModel
import ch.bailu.gtk.model.StructureModel

fun filterValues(value: String): Boolean {
    return ("2147483648" != value
            && "9223372036854775807" != value
            && "4294967295" != value
            && "18446744073709551615" != value
            && "-9223372036854775808" != value
            && "86400000000" != value
            && "3600000000" != value)
}

fun filterMethod(structureModel: StructureModel, methodModel: MethodModel): Boolean {
    if ("MenuItem" == structureModel.apiName && "activate" == methodModel.apiName) {
        return false
    }
    if ("ActionRow" == structureModel.apiName && "activate" == methodModel.apiName) {
        return false
    }
    if ("ToolPalette" == structureModel.apiName && "getStyle" == methodModel.apiName) {
        return false
    }
    if ("Toolbar" == structureModel.apiName && "getStyle" == methodModel.apiName) {
        return false
    }
    if ("Coverage" == structureModel.apiName && "ref" == methodModel.apiName) {
        return false
    }
    if ("PrintUnixDialog" == structureModel.apiName && "getSettings" == methodModel.apiName) {
        return false
    }
    return !("PrintSettings" == structureModel.apiName && "get" == methodModel.apiName)
}


fun filterField(structureModel: StructureModel): Boolean {
    if ("PixbufAnimationIterClass" == structureModel.apiName) {
        return false
    }
    if ("SettingsBackendClass" == structureModel.apiName) {
        return false
    }
    if ("PixbufFormat" == structureModel.apiName) {
        return false
    }
    if ("PixbufModule" == structureModel.apiName) {
        return false
    }
    return "PixbufModulePattern" != structureModel.apiName
}

/**
 * List of records that support malloc constructor
 */
private val MALLOC = arrayOf(
        "RGBA",
        "Rectangle",
        "Matrix",
        "TreeIter",
        "TextIter",
        "Value"
)


fun filterCreateMallocConstructor(structureModel: StructureModel): Boolean {
    for (s in MALLOC) {
        if (s == structureModel.apiName) {
            return true
        }
    }
    return false
}

fun filterFieldDirectAccess(structureModel: StructureModel): Boolean {
    return "AttrShape" == structureModel.apiName || "Event" == structureModel.apiName
}
