package ch.bailu.gtk.parser.tag

import ch.bailu.gtk.builder.BuilderInterface

class DocumentTag(builder: BuilderInterface): Tag() {
    val rootBuilder = builder

    override fun getBuilder(): BuilderInterface {
        return rootBuilder
    }

    override fun getChild(name: String, prefix: String): TagWithParent {
        if ("repository" == name) {
            return RepositoryTag(this)
        }
        return ignore()
    }

    override fun getParent(): Tag {
        return this
    }
}