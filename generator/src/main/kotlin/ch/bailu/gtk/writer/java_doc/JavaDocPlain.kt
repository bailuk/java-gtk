package ch.bailu.gtk.writer.java_doc

import ch.bailu.gtk.model.MethodModel
import ch.bailu.gtk.model.StructureModel
import ch.bailu.gtk.table.NamespaceTable
import java.io.Writer

class JavaDocPlain(writer: Writer): JavaDoc(writer) {
    override fun writeBlock(doc: String) {
        doc.lines().forEach {
            writeLine(it, "")
        }
    }

    override fun writeClassUrl(structureModel: StructureModel) {
        NamespaceTable.with(structureModel.nameSpaceModel.getNamespace()) {
            writeLine(it.docUrl.getUrl(structureModel), "")
        }

    }

    override fun writeParameter(methodModel: MethodModel) {
        methodModel.parameters.forEach {
            writeDocParamLine(it.doc, "@param ${it.name}")
        }
    }

    override fun writeReturn(methodModel: MethodModel) {
        if (!methodModel.returnType.isVoid) {
            writeDocParamLine(methodModel.returnType.doc, "@return")
        }
    }


    private fun writeDocParamLine(doc: String, prefix: String) {
        startLine().a(prefix)
        doc.lines().forEach{a(" ").a(it)}
        a("\n")
    }
}