package ch.bailu.gtk.parser.tag

import ch.bailu.gtk.NamespaceConfig
import ch.bailu.gtk.builder.BuilderInterface

class DocumentTag(private val builder: BuilderInterface,
                  private val namespaceConfig: NamespaceConfig
): Tag() {

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
