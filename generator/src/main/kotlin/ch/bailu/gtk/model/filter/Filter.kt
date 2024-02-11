package ch.bailu.gtk.model.filter

import ch.bailu.gtk.model.MethodModel
import ch.bailu.gtk.model.StructureModel

/**
 * See [ch.bailu.gtk.table.ValueTable] for converted values
 */
fun filterValues(value: String): Boolean {
    return ("9223372036854775807" != value     // G_MAXINT64
            && "-9223372036854775808" != value // G_MININT64
            && "86400000000" != value          // TIME_SPAN_DAY
            && "3600000000" != value)          // TIME_SPAN_HOUR
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
