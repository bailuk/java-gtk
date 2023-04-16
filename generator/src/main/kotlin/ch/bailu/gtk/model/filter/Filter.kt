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
    if ("PrintUnixDialog" == structureModel.apiName && "getSettings" == methodModel.apiName) {
        return false
    }
    if ("ActionRow" == structureModel.apiName && "activate" == methodModel.apiName) {
        return false
    }
    return true
}


fun filterField(): Boolean {
    return true
}

fun filterCreateMallocConstructor(structureModel: StructureModel): Boolean {
    return structureModel.allFieldsAreSupported && !structureModel.disguised
}

fun filterFieldDirectAccess(): Boolean {
    return true
}
