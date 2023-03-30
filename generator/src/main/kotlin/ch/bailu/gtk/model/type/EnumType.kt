package ch.bailu.gtk.model.type

import ch.bailu.gtk.parser.tag.ParameterTag
import ch.bailu.gtk.table.EnumTable

object EnumType {
    private const val OUT_ENUM_C_TYPE = "gint*"

    fun toCTypeName(namespace: String, parameter: ParameterTag): String {
        return if (isOutEnum(namespace, parameter)) OUT_ENUM_C_TYPE else parameter.getType()
    }

    fun isOutEnum(namespace: String, parameter: ParameterTag): Boolean {
        return !parameter.inDirection && EnumTable.isEnum(namespace, parameter)
    }
}
