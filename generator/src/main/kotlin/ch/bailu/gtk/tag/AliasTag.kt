package ch.bailu.gtk.tag

class AliasTag(parent: TagWithParent): ParameterTag(parent) {

    override fun end() {
        getBuilder().buildAlias(this)
    }
}