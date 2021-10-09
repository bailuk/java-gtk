package ch.bailu.gtk.parser.tag

class RepositoryTag(parent: Tag) : TagWithParent(parent) {
    private val namespace = NamespaceTag(this)

    override fun getChild(name: String, prefix: String): TagWithParent {
        if ("namespace" == name) {
            return namespace
        }
        if ("package" == name) {
            return namespace.addPackage(NamedWithDocTag(this))
        }
        if ("c" == prefix && "include" == name) {
            return namespace.addInclude(NamedWithDocTag(this))
        }
        return ignore()
    }
}
