package ch.bailu.gtk.tag

class IgnoreTag (parent: Tag) : TagWithParent(parent){

    override fun getChild(name: String, prefix: String): TagWithParent {
        return ignore()
    }
}