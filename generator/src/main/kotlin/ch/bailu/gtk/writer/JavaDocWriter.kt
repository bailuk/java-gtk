package ch.bailu.gtk.writer

import ch.bailu.gtk.model.MethodModel
import ch.bailu.gtk.model.Model
import ch.bailu.gtk.model.ParameterModel
import ch.bailu.gtk.model.StructureModel
import ch.bailu.gtk.writer.java_doc.JavaDoc
import ch.bailu.gtk.writer.java_doc.JavaDocHtml
import java.io.Writer


class JavaDocWriter(writer: Writer, val doc: JavaDoc) : CodeWriter(writer) {

    override fun writeClass(structureModel: StructureModel) {
        doc.writeStart(0)
        doc.writeClassUrl(structureModel)
        doc.writeBlock(structureModel.doc)
        doc.writeDocEnd()
    }

    override fun writeInterface(structureModel: StructureModel) {
        writeClass(structureModel)
    }

    override fun writeInternalConstructor(structureModel: StructureModel) {}

    override fun writeConstructor(structureModel: StructureModel, methodModel: MethodModel) {
        if (methodModel.getParameters().isNotEmpty() || methodModel.doc.length > 3) {
            doc.writeStart(4)
            doc.writeBlock(methodModel.doc)
            doc.writeParameter(methodModel)
            doc.writeDocEnd()
        }
    }

    override fun writeFactory(structureModel: StructureModel, methodModel: MethodModel) {
        writeNativeMethod(structureModel, methodModel)
    }

    override fun writePrivateFactory(structureModel: StructureModel, methodModel: MethodModel) {
        writeNativeMethod(structureModel, methodModel)
    }

    override fun writeConstant(parameterModel: ParameterModel) {
        doc.writeStart(4)
        doc.writeBlock(parameterModel.doc)
        doc.writeDocEnd()
    }

    override fun writeNativeMethod(structureModel: StructureModel, methodModel: MethodModel) {
        if (methodModel.getParameters().isNotEmpty() || methodModel.doc.length > 3 || !methodModel.returnType.isVoid) {
            doc.writeStart(4)
            doc.writeBlock(methodModel.doc)
            doc.writeParameter(methodModel)
            doc.writeReturn(methodModel)
            doc.writeDocEnd()
        }
    }

    override fun writeSignal(structureModel: StructureModel, methodModel: MethodModel) {
        if (methodModel.getParameters().isNotEmpty() || methodModel.doc.length > 3 || !methodModel.returnType.isVoid) {
            doc.writeStart(8)
            doc.writeBlock(methodModel.doc)
            doc.writeParameter(methodModel)
            doc.writeReturn(methodModel)
            doc.writeDocEnd()
        }
    }

    override fun writeField(structureModel: StructureModel, parameterModel: ParameterModel) {
        //writeConstant(structureModel, parameterModel)
    }

    override fun writeFunction(structureModel: StructureModel, methodModel: MethodModel) {
        writeNativeMethod(structureModel, methodModel)
    }

    override fun writeUnsupported(model: Model) {}

    override fun writeEnd() {}

    override fun writeMallocConstructor(structureModel: StructureModel) {}

    override fun writeCallback(structureModel: StructureModel, methodModel: MethodModel) {
        writeSignal(structureModel, methodModel)
    }
}