package ch.bailu.gtk.parser.tag

import ch.bailu.gtk.NamespaceConfig
import ch.bailu.gtk.builder.BuilderInterface

abstract class Tag {
    abstract fun getChild(name: String): TagWithParent

    open fun started() {}
    open fun end() {}

    open fun setText(text: String) {}
    open fun setAttribute(name: String, value: String) {}

    abstract fun getParent(): Tag

    abstract fun getBuilder(): BuilderInterface
    abstract fun getNamespaceConfig(): NamespaceConfig


    protected fun ignore(): IgnoreTag {
        return IgnoreTag(this)
    }
}