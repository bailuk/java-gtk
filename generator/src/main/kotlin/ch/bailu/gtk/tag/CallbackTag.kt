package ch.bailu.gtk.tag

import java.io.IOException

class CallbackTag(parent: TagWithParent): MethodTag(parent) {

    @Throws(IOException::class)
    override fun end() {
        getBuilder().buildCallback(this)
    }
}