package ch.bailu.gtk.parser.tag

import ch.bailu.gtk.builder.BuilderInterface
import ch.bailu.gtk.config.NamespaceConfig

abstract class TagWithParent (parent: Tag): Tag() {

    private var parent = parent

    override fun getParent(): Tag { return parent}


    override fun getBuilder(): BuilderInterface {
        return parent.getBuilder()
    }

    override fun getNamespaceConfig() : NamespaceConfig {
        return parent.getNamespaceConfig()
    }
}