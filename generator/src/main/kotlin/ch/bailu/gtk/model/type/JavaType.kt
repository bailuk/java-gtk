package ch.bailu.gtk.model.type

import ch.bailu.gtk.table.PrimitivesTable

class JavaType(t: String) {
    private var type = PrimitivesTable.convert(t)

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
