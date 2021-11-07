package ch.bailu.gtk.writer

import java.io.Writer

class TextWriter(private val writer : Writer) {
    private var space = 0

    fun a(o : String) : TextWriter {
        writer.append(o)
        return this
    }

    fun a(o : String, intent: Int) : TextWriter {
        writer.append(o.replaceIndent(" ".repeat(intent)))
        return nl()
    }

    fun start(i : Int) : TextWriter {
        space = Math.max(space, i)
        writer.write("\n".repeat(space))
        return this
    }

    fun end(i : Int) : TextWriter {
        space = i
        return this
    }

    fun nl(): TextWriter {
        return nl(1)
    }

    fun nl(i : Int): TextWriter {
        return a("\n".repeat(i))
    }
}