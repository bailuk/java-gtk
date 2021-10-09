package ch.bailu.gtk.model.type

import ch.bailu.gtk.table.PrimitivesTable
import ch.bailu.gtk.parser.tag.ParameterTag

class JavaType {
    private var type: String

    constructor(parameter: ParameterTag) {
        type = PrimitivesTable.convert(parameter.getType())
    }

    constructor(t: String) {
        type = PrimitivesTable.convert(t)
    }


    fun isValid(): Boolean {
        return "" != type
    }

    fun getType(): String {
        return type
    }

    fun isVoid(): Boolean {
        return "void" == type
    }
}