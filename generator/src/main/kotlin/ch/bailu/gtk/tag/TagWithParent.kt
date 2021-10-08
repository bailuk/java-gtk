package ch.bailu.gtk.tag

import ch.bailu.gtk.builder.BuilderInterface

abstract class TagWithParent (parent: Tag): Tag() {

    private var parent = parent

    override fun getParent(): Tag { return parent}


    override fun getBuilder(): BuilderInterface {
        return parent.getBuilder()
    }
}