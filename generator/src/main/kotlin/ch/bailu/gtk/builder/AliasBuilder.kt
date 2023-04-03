package ch.bailu.gtk.builder

import ch.bailu.gtk.NamespaceConfig
import ch.bailu.gtk.model.type.NamespaceType
import ch.bailu.gtk.parser.tag.*
import ch.bailu.gtk.table.*
import ch.bailu.gtk.table.AliasTable.convert


class AliasBuilder : BuilderInterface{
    private var namespace = ""

    override fun buildStructure(structure: StructureTag) {
        StructureTable.add(namespace, convert(namespace, structure.getName()), structure.getType.isNotEmpty())
    }

    private fun convert(namespace: String, name: String): String {
        return convert(NamespaceType(namespace, name)).name
    }

    override fun buildNamespaceStart(namespace: NamespaceTag, namespaceConfig: NamespaceConfig) {
        this.namespace = namespace.getName().lowercase()
        NamespaceTable.add(this.namespace, namespaceConfig)
    }

    override fun buildNamespaceEnd() {}

    override fun buildAlias(aliasTag: AliasTag) {
        AliasTable.add(namespace, aliasTag.getName(), aliasTag.getTypeName())
    }

    override fun buildEnumeration(enumeration: EnumerationTag) {
        EnumTable.add(NamespaceType(namespace, enumeration.getName()))
        EnumTable.add(NamespaceType(namespace, enumeration.type))
    }

    override fun buildCallback(callbackTag: CallbackTag) {
        CallbackTable.add(namespace, callbackTag)
    }
}
