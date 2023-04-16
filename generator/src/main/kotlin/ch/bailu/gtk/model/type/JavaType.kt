package ch.bailu.gtk.model.type

import ch.bailu.gtk.log.DebugPrint
import ch.bailu.gtk.table.PrimitivesTable

/**
 * A Primitive java type.
 * Is valid if name is configured in [PrimitivesTable]
 */
class JavaType(typeName: String) : Type() {
    private var type = PrimitivesTable.convert(typeName)

    val valid: Boolean
        get () = type.isNotEmpty()

    fun getApiTypeName(): String {
        return type
    }

    fun isVoid(): Boolean {
        return "void" == type
    }

    override fun getDebugIdentifier(): String {
        return "j"
    }

    override fun toString(): String {
        return DebugPrint.colon(this, type)
    }
}
