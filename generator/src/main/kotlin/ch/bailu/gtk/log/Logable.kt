package ch.bailu.gtk.log

import java.io.Writer

interface Logable {
    fun log(writer: Writer)
}