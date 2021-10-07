package ch.bailu.gtk.tag

import ch.bailu.gtk.builder.BuilderInterface

abstract class Tag {
    abstract fun getChild(name: String, prefix: String): TagWithParent

    open fun started() {}
    open fun end() {}

    open fun setText(text: String) {}
    open fun setAttribute(name: String, value: String) {}

    abstract fun getParent(): Tag

    abstract fun getBuilder(): BuilderInterface


    protected fun ignore(): IgnoreTag {
        return IgnoreTag(this)
    }
}