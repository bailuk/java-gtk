package ch.bailu.gtk.writer;

import java.io.Writer

class GroupSpace(out : Writer) {
    private var count = 0;
    private var print = false
    private val out   = out

    fun start(c : Int) {
        end(c)
        start()
    }

    fun end(c: Int) {
        count = Math.max(c, count)
    }

    fun next() {
        print = true
    }

    fun start() {
        if (print) {
            out.append("\n".repeat(count))
            count = 0
            print = false
        }
    }
}
