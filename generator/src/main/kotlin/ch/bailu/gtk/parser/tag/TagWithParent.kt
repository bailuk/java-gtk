package ch.bailu.gtk.parser.tag

import ch.bailu.gtk.NamespaceConfig
import ch.bailu.gtk.builder.BuilderInterface

abstract class TagWithParent (private var parent: Tag): Tag() {

    override fun getParent(): Tag { return parent}

    override fun getBuilder(): BuilderInterface {
        return parent.getBuilder()
    }

    override fun getNamespaceConfig() : NamespaceConfig {
        return parent.getNamespaceConfig()
    }
}