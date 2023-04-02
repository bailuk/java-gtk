package ch.bailu.gtk.model.type

import ch.bailu.gtk.parser.tag.ParameterTag
import ch.bailu.gtk.table.EnumTable

object CTypeConverter {
    private const val OUT_ENUM_C_TYPE = "gint*"
    private const val VARARG_TYPE = "..."

    fun toCTypeName(namespace: String, parameter: ParameterTag): String {
        return if (isOutEnum(namespace, parameter)) {
            OUT_ENUM_C_TYPE
        } else if (parameter.isVarargs) {
            VARARG_TYPE
        } else {
            parameter.getType()
        }
    }

    private fun isOutEnum(namespace: String, parameter: ParameterTag): Boolean {
        return !parameter.inDirection && EnumTable.isEnum(namespace, parameter)
    }
}
