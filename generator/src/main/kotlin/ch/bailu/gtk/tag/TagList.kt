package ch.bailu.gtk.tag

import java.util.*

class TagList<T : TagWithParent> : ArrayList<T>() {
    fun addTag(tag: T): TagWithParent {
        this.add(tag)
        return tag
    }
}