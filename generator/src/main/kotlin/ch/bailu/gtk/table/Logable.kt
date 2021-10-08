package ch.bailu.gtk.table

import java.io.Writer

interface Logable {
    fun log(writer: Writer)
}