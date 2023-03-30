package ch.bailu.gtk.model.type

import ch.bailu.gtk.table.PrimitivesTable

/**
 * A Primitive java type.
 * Is valid if name is configured in [PrimitivesTable]
 */
class JavaType(typeName: String) {
    private var type = PrimitivesTable.convert(typeName)

    val valid: Boolean
        get () = type.isNotEmpty()

    fun getApiTypeName(): String {
        return type
    }

    fun isVoid(): Boolean {
        return "void" == type
    }
}
