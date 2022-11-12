package ch.bailu.gtk.writer.java_doc

import ch.bailu.gtk.model.MethodModel
import ch.bailu.gtk.model.StructureModel
import ch.bailu.gtk.writer.Append
import java.io.Writer

abstract class JavaDoc(val writer: Writer): Append {
    private var begin = ""
    private var space = ""
    private var intent = 0

    override fun a(o: String): Append {
        writer.append(o)
        return this
    }


    override fun a(o : String, intent: Int) : Append {
        writer.append(o.replaceIndent(" ".repeat(intent)))
        return this
    }


    abstract fun writeClassUrl(structureModel: StructureModel)
    abstract fun writeBlockPlain(doc: String)
    abstract fun writeBlock(doc: String)
    abstract fun writeParameter(methodModel: MethodModel)
    abstract fun writeReturn(methodModel: MethodModel)

    fun writeLine(line: String, nl: String) {
        a("${begin}${nl}${line}\n")
    }

    fun writeStart(intend: Int) {
        intent = intend
        space = " ".repeat(intent)
        begin = "$space * "

        a("${space}/**\n")
    }

    fun writeDocEnd() {
        a("${space}*/\n")
    }

    fun startLine(): Append {
        return a(begin)
    }
}
