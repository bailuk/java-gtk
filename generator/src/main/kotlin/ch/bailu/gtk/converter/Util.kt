package ch.bailu.gtk.converter

import ch.bailu.gtk.table.EnumTable
import ch.bailu.gtk.parser.tag.ParameterTag


fun isEnum(namespace: String, parameter: ParameterTag): Boolean {
    return isEnum(namespace, parameter.getTypeName()) || isEnum(namespace, parameter.getType())
}

private fun isEnum(namespace: String, typeName: String): Boolean {
    return EnumTable.contains(NamespaceType(namespace, typeName))
}
