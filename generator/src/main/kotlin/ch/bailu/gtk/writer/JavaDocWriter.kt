package ch.bailu.gtk.writer

import ch.bailu.gtk.model.MethodModel
import ch.bailu.gtk.model.Model
import ch.bailu.gtk.model.ParameterModel
import ch.bailu.gtk.model.StructureModel
import ch.bailu.gtk.table.NamespaceTable
import java.io.Writer


class JavaDocWriter(writer: Writer) : CodeWriter(writer) {

    private var space = ""
    private var begin = ""
    private var intent = 0

    private fun writeDocBlock(doc: String) {
        val evenPre = isEvenPre(doc)
        var nl = ""

        if (evenPre)
            doc.lines().forEach {
                if (it.contains("|[")) nl = ""
                writeDocLine(replacePre(escapeDoc(it)), nl)
                if (it.contains("]|")) nl = "<br>"
        } else {
            doc.lines().forEach {
                writeDocLine(escapeDoc(it), nl)
                nl = "<br>"
            }
        }
    }

    private fun isEvenPre(doc: String): Boolean {
        if (count(doc, "|[") != count(doc, "]|")) {
            println("WARNING code tags are not even: \"${doc.replace('\n', ' ')}\"")
            return false
        } else {
            return true
        }
    }

    private fun writeDocParamBlock(methodModel: MethodModel) {
        methodModel.getParameters().forEach {
            writeDocParamLine(it.doc, "@param ${it.name}")
        }
    }

    private fun writeDocReturnBlock(methodModel: MethodModel) {
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

    private fun count(doc: String, word: String): Int {
        var index = doc.indexOf(word)
        var count = 0

        while (index > -1) {
            count++
            index = doc.indexOf(word, index+1)
        }
        return count
    }

    private fun writeDocParamLine(doc: String, prefix: String) {
        val lines = doc.lines()
        a(begin).a(prefix)
        lines.forEach{
            a(" ").a(escapeDoc(it))
        }
        a("\n")
    }

    private fun writeDocLine(line: String, nl: String) {
        a("${begin}${nl}${line}\n")
    }

    private fun writeDocStart(intend: Int) {
        intent = intend
        space = " ".repeat(intent);
        begin = "${space} * "

        a("${space}/**\n")
    }

    private fun writeDocEnd() {
        a("${space}*/\n")
    }

    override fun writeClass(structureModel: StructureModel) {
        writeDocStart(0)
        writeDocClassUrl(structureModel)
        writeDocBlock(structureModel.doc)
        writeDocEnd()
    }


    fun writeDocClassUrl(structureModel: StructureModel) {
        NamespaceTable.with(structureModel.nameSpaceModel.getNamespace()) {
            writeDocLine("<p><b><a href=\"${it.docUrl.getUrl(structureModel)}\">External documentation</a></b></p>", "")
        }

    }

    override fun writeInterface(structureModel: StructureModel) {
        writeClass(structureModel)
    }

    override fun writeInternalConstructor(structureModel: StructureModel) {}

    override fun writeConstructor(structureModel: StructureModel, methodModel: MethodModel) {
        if (methodModel.getParameters().isNotEmpty() || methodModel.doc.length > 3) {
            writeDocStart(4)
            writeDocBlock(methodModel.doc)
            writeDocParamBlock(methodModel)
            writeDocEnd()
        }
    }

    override fun writeFactory(structureModel: StructureModel, methodModel: MethodModel) {
        writeNativeMethod(structureModel, methodModel)
    }

    override fun writePrivateFactory(structureModel: StructureModel, methodModel: MethodModel) {
        writeNativeMethod(structureModel, methodModel)
    }

    override fun writeConstant(parameterModel: ParameterModel) {
        writeDocStart(4)
        writeDocBlock(parameterModel.doc)
        writeDocEnd()
    }

    override fun writeNativeMethod(structureModel: StructureModel, methodModel: MethodModel) {
        if (methodModel.getParameters().isNotEmpty() || methodModel.doc.length > 3 || !methodModel.returnType.isVoid) {
            writeDocStart(4)
            writeDocBlock(methodModel.doc)
            writeDocParamBlock(methodModel)
            writeDocReturnBlock(methodModel)
            writeDocEnd()
        }
    }

    override fun writeSignal(structureModel: StructureModel, methodModel: MethodModel) {
        if (methodModel.getParameters().isNotEmpty() || methodModel.doc.length > 3 || !methodModel.returnType.isVoid) {
            writeDocStart(8)
            writeDocBlock(methodModel.doc)
            writeDocParamBlock(methodModel)
            writeDocReturnBlock(methodModel)
            writeDocEnd()
        }
    }

    override fun writeField(structureModel: StructureModel, parameterModel: ParameterModel) {
        //writeConstant(structureModel, parameterModel)
    }

    override fun writeFunction(structureModel: StructureModel, methodModel: MethodModel) {
        writeNativeMethod(structureModel, methodModel)
    }

    override fun writeUnsupported(model: Model) {
    }

    override fun writeEnd() {
    }

    override fun writeMallocConstructor(structureModel: StructureModel) {
    }

    override fun writeCallback(structureModel: StructureModel, methodModel: MethodModel) {
        writeSignal(structureModel, methodModel)
    }
}