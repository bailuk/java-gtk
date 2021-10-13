package ch.bailu.gtk.writer.java_doc

import ch.bailu.gtk.model.MethodModel
import ch.bailu.gtk.model.StructureModel
import ch.bailu.gtk.table.NamespaceTable
import java.io.Writer

class JavaDocHtml(writer: Writer) : JavaDoc(writer) {

    override fun writeBlock(doc: String) {
        val evenPre = isEvenPre(doc)
        var nl = ""
        var insideCodeBlock = false

        doc.lines().forEach {
            if (evenPre && it.contains("|[")) insideCodeBlock = true

            if (insideCodeBlock) {
                writeLine(replacePre(escapeDoc(it)), "")
            } else {
                writeLine(replacePre(escapeDoc(it)), nl)
            }

            if (it.contains("]|")) insideCodeBlock = false
            nl = "<br>"
        }
    }



    override fun writeClassUrl(structureModel: StructureModel) {
        NamespaceTable.with(structureModel.nameSpaceModel.getNamespace()) {
            writeLine("<p><a href=\"${it.docUrl.getUrl(structureModel)}\">${it.docUrl.getUrl(structureModel)}</a></p>", "")
        }

    }

    private fun isEvenPre(doc: String): Boolean {
        return if (count(doc, "|[") != count(doc, "]|")) {
            println("WARNING code tags are not even: \"${doc.replace('\n', ' ')}\"")
            false
        } else {
            true
        }
    }

    private fun count(doc: String, word: String): Int {
        var index = doc.indexOf(word)
        var count = 0

        while (index > -1) {
            count++
            index = doc.indexOf(word, index+1)
        }
        return count
    }


    override fun writeParameter(methodModel: MethodModel) {
        methodModel.getParameters().forEach {
            writeDocParamLine(it.doc, "@param ${it.name}")
        }
    }

    override fun writeReturn(methodModel: MethodModel) {
        if (!methodModel.returnType.isVoid) {
            writeDocParamLine(methodModel.returnType.doc, "@return")
        }
    }


    private fun escapeDoc(doc: String): String {
        return doc
                .replace("&",  "&amp;")
                .replace("\"", "&quot;")
                .replace("<",  "&lt;")
                .replace(">",  "&gt;")
                .replace("#",  "&#35;")
                .replace("@",  "&#64;")
                .replace("\\", "&#92;")
    }

    private fun replacePre(doc: String): String {
        return doc.replace("|[", "<pre>").replace("]|", "</pre>")
    }

    private fun writeDocParamLine(doc: String, prefix: String) {
        val lines = doc.lines()
        startLine().a(prefix)
        lines.forEach{
            a(" ").a(escapeDoc(it))
        }
        a("\n")
    }
}
