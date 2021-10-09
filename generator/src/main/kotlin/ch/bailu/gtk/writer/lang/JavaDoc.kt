package ch.bailu.gtk.writer.lang

import ch.bailu.gtk.model.MethodModel
import ch.bailu.gtk.model.Model
import ch.bailu.gtk.model.ParameterModel
import ch.bailu.gtk.model.StructureModel
import ch.bailu.gtk.writer.CodeWriter
import java.io.Writer


class JavaDoc(writer: Writer) : CodeWriter(writer) {

    private var space = ""
    private var begin = ""
    private var intent = 0

    private fun writeDocBlock(doc: String) {
        var nl = "<br>"
        doc.lines().forEach {
            if (it.contains("|[")) nl = ""
            writeDocLine(escapeDoc(it), nl)
            if (it.contains("]|")) nl = "<br>"
        }
    }

    private fun writeDocParamBlock(methodModel: MethodModel) {
        methodModel.getParameters().forEach {
            writeDocParamLine(it.doc, "@param ${it.name}")
        }
        if (!methodModel.returnType.isVoid) {
            writeDocParamLine(methodModel.returnType.doc, "@return")
        }
    }

    private fun escapeDoc(doc: String): String {
        return doc
                .replace("&", "&amp;")
                .replace("\"", "&quot;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\\", "&#92;")
                .replace("|[", "<pre> ")
                .replace("]|", " </pre>")
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
        if (structureModel.doc.length > 5) {
            writeDocStart(0)
            if (structureModel.isClassType) {
                // TODO fix link
                writeDocLine("<a href=\"https://docs.gtk.org/gtk3/class.${structureModel.apiName}.html\">Gtk.${structureModel.apiName}</a>", "<br>")
            }
            writeDocBlock(structureModel.doc)
            writeDocEnd()
        }
    }

    override fun writeInterface(structureModel: StructureModel) {
        writeClass(structureModel)
    }

    override fun writeInternalConstructor(structureModel: StructureModel) {}

    override fun writeConstructor(structureModel: StructureModel, methodModel: MethodModel) {
        writeNativeMethod(structureModel, methodModel)
    }

    override fun writeFactory(structureModel: StructureModel, methodModel: MethodModel) {
        writeNativeMethod(structureModel, methodModel)
    }

    override fun writePrivateFactory(structureModel: StructureModel, methodModel: MethodModel) {
        writeNativeMethod(structureModel, methodModel)
    }

    override fun writeConstant(parameterModel: ParameterModel) {
        writeDocStart(4)
        writeDocParamLine(parameterModel.doc, "@param ${parameterModel.name}")
        writeDocEnd()
    }

    override fun writeNativeMethod(structureModel: StructureModel, methodModel: MethodModel) {
        if (methodModel.getParameters().isNotEmpty() || methodModel.doc.length > 3 || !methodModel.returnType.isVoid) {
            writeDocStart(4)
            writeDocBlock(methodModel.doc)
            writeDocParamBlock(methodModel)
            writeDocEnd()
        }
    }

    override fun writeSignal(structureModel: StructureModel, methodModel: MethodModel) {
        writeNativeMethod(structureModel, methodModel)
    }

    override fun writeField(structureModel: StructureModel, parameterModel: ParameterModel) {
        //writeConstant(structureModel, parameterModel)
    }

    override fun writeFunction(structureModel: StructureModel, methodModel: MethodModel) {
        writeNativeMethod(structureModel, methodModel)
    }

    override fun writeUnsupported(model: Model) {
        TODO("Not yet implemented")
    }

    override fun writeEnd() {
        TODO("Not yet implemented")
    }

    override fun writeMallocConstructor(structureModel: StructureModel) {
        TODO("Not yet implemented")
    }

    override fun writeCallback(structureModel: StructureModel, methodModel: MethodModel) {
        writeNativeMethod(structureModel, methodModel)
    }
}