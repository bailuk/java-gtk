package ch.bailu.gtk.parser.tag

import ch.bailu.gtk.builder.BuilderInterface
import ch.bailu.gtk.config.NamespaceConfig

class DocumentTag(builder: BuilderInterface, namespaceConfig: NamespaceConfig): Tag() {
    private val builder = builder
    private val namespaceConfig = namespaceConfig

    override fun getBuilder(): BuilderInterface {
        return builder
    }

    override fun getNamespaceConfig(): NamespaceConfig {
        return namespaceConfig
    }

    override fun getChild(name: String): TagWithParent {
        if ("repository" == name) {
            return RepositoryTag(this)
        }
        return ignore()
    }

    override fun getParent(): Tag {
        return this
    }

}