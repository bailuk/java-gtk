package ch.bailu.gtk.model.type

import ch.bailu.gtk.log.DebugPrint
import java.util.regex.Pattern

class CType(type: String) : Type() {
    companion object {
        private val P_CONST = Pattern.compile("^const [A-Za-z]+")
        private val P_POINTER = Pattern.compile(".*[A-Za-z]+\\*$")
        private val P_NOPOINTER = Pattern.compile(".*[A-Za-z]+$")
    }

    val type = if ("" == type) "void*" else type

    val isSinglePointer = P_POINTER.matcher(this.type).find()
    val isConst         = P_CONST.matcher(this.type).find()
    val isDirectType    = P_NOPOINTER.matcher(this.type).find()

    operator fun contains(type: String): Boolean {
        return this.type.contains(type)
    }

    override fun getDebugIdentifier(): String {
        return "c"
    }

    override fun toString(): String {
        return DebugPrint.colon(this, type)
    }
}
