package ch.bailu.gtk.builder

import ch.bailu.gtk.parser.tag.*
import ch.bailu.gtk.config.NamespaceConfig
import java.io.IOException

interface BuilderInterface {
    @Throws(IOException::class)
    fun buildStructure(structure: StructureTag)
    fun buildNamespaceStart(namespace: NamespaceTag, namespaceConfig: NamespaceConfig)

    @Throws(IOException::class)
    fun buildNamespaceEnd()
    fun buildAlias(aliasTag: AliasTag)

    @Throws(IOException::class)
    fun buildEnumeration(enumeration: EnumerationTag)

    @Throws(IOException::class)
    fun buildCallback(callbackTag: CallbackTag)
    
    fun buildErrorStubs(enabled: Boolean)
}
