package ch.bailu.gtk.builder

import ch.bailu.gtk.tag.*
import java.io.IOException

interface BuilderInterface {
    @Throws(IOException::class)
    fun buildStructure(structure: StructureTag)
    fun buildNamespaceStart(namespace: NamespaceTag)

    @Throws(IOException::class)
    fun buildNamespaceEnd(namespace: NamespaceTag)
    fun buildAlias(alias: AliasTag)

    @Throws(IOException::class)
    fun buildEnumeration(enumeration: EnumerationTag)

    @Throws(IOException::class)
    fun buildCallback(callbackTag: CallbackTag)
}