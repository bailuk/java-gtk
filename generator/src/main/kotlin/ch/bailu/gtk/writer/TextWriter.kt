package ch.bailu.gtk.writer

import java.io.Writer

class TextWriter(private val writer : Writer) {
    private var space = 0

    fun a(text : String) : TextWriter {
        writer.append(text)
        return this
    }

    fun a(text : String, intent: Int) : TextWriter {
        writer.append(text.replaceIndent(" ".repeat(intent)))
        return nl()
    }

    fun start(i : Int) : TextWriter {
        space = Math.max(space, i)
        return nl(space)
    }

    fun end(i : Int) {
        space = i
    }

    fun nl(i : Int = 1): TextWriter {
        return a("\n".repeat(i))
    }

    fun l(s : Int, text : String, e : Int) {
        start(s)
        a(text).nl()
        end(e)
    }
}