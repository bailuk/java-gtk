package ch.bailu.gtk.writer.java_doc

import ch.bailu.gtk.model.MethodModel
import ch.bailu.gtk.model.StructureModel
import java.io.Writer

class JavaDocEmpty(writer: Writer) : JavaDoc(writer) {
    override fun writeClassUrl(structureModel: StructureModel) {}
    override fun writeBlock(doc: String) {}
    override fun writeParameter(methodModel: MethodModel) {}
    override fun writeReturn(methodModel: MethodModel) {}
}