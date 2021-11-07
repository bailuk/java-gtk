package ch.bailu.gtk.writer;

interface Append {
    fun a(o : String) : Append
    fun a(o : String, intent: Int) : Append
}
