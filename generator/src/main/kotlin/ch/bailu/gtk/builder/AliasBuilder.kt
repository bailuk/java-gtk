package ch.bailu.gtk.builder

import ch.bailu.gtk.NamespaceConfig
import ch.bailu.gtk.converter.NamespaceType
import ch.bailu.gtk.parser.tag.*
import ch.bailu.gtk.table.AliasTable.add
import ch.bailu.gtk.table.AliasTable.convert
import ch.bailu.gtk.table.CallbackTable.add
import ch.bailu.gtk.table.EnumTable.add
import ch.bailu.gtk.table.NamespaceTable.add
import ch.bailu.gtk.table.StructureTable.add

class AliasBuilder : BuilderInterface{
    private var namespace = ""

    override fun buildStructure(structure: StructureTag) {
        add(namespace, convert(namespace, structure.getName()))
    }

    private fun convert(namespace: String, name: String): String {
        return convert(NamespaceType(namespace, name)).name
    }

    override fun buildNamespaceStart(namespace: NamespaceTag, namespaceConfig: NamespaceConfig) {
        this.namespace = namespace.getName().lowercase()
        add(this.namespace, namespaceConfig)
    }

    override fun buildNamespaceEnd() {}

    override fun buildAlias(aliasTag: AliasTag) {
        add(namespace, aliasTag.getName(), aliasTag.getTypeName())
    }

    override fun buildEnumeration(enumeration: EnumerationTag) {
        add(NamespaceType(namespace, enumeration.getName()))
        add(NamespaceType(namespace, enumeration.type))
    }

    override fun buildCallback(callbackTag: CallbackTag) {
        add(namespace, callbackTag)
    }
}
