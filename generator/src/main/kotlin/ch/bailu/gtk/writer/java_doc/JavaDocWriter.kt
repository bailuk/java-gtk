package ch.bailu.gtk.writer.java_doc

import ch.bailu.gtk.model.*
import ch.bailu.gtk.model.filter.ModelList
import ch.bailu.gtk.writer.CodeWriter
import ch.bailu.gtk.writer.TextWriter
import sun.misc.Signal


class JavaDocWriter(private val out: TextWriter, val doc: JavaDoc) : CodeWriter {
    override fun writeClass(structureModel: StructureModel) {
        writeClassOrInterface(structureModel)
    }

    private fun writeClassOrInterface(structureModel: StructureModel) {
        doc.writeStart(0)
        doc.writeBlock(structureModel.doc)
        doc.writeClassUrl(structureModel)
        doc.writeDocEnd()
    }

    override fun writeInterface(structureModel: StructureModel) {
        writeClassOrInterface(structureModel)
    }

    override fun writeConstructor(structureModel: StructureModel, methodModel: MethodModel) {
        if (methodModel.parameters.isNotEmpty() || methodModel.doc.length > 3) {
            doc.writeStart(4)
            doc.writeBlock(methodModel.doc)
            doc.writeParameter(methodModel)
            doc.writeDocEnd()
        }
    }

    override fun writeFactory(structureModel: StructureModel, methodModel: MethodModel) {
        writeMethod(structureModel, methodModel)
    }

    override fun writePrivateFactory(structureModel: StructureModel, methodModel: MethodModel) {
        writeMethod(structureModel, methodModel)
    }

    override fun writeConstant(structureModel: StructureModel, parameterModel: ParameterModel) {
        doc.writeStart(4)
        doc.writeBlock(parameterModel.doc)
        doc.writeDocEnd()
    }

    override fun writeMethod(structureModel: StructureModel, methodModel: MethodModel) {
        if (methodModel.parameters.isNotEmpty() || methodModel.doc.length > 3 || !methodModel.returnType.isVoid) {
            doc.writeStart(4)
            doc.writeBlock(methodModel.doc)
            doc.writeParameter(methodModel)
            doc.writeReturn(methodModel)
            doc.writeDocEnd()
        }
    }

    override fun writeSignal(structureModel: StructureModel, methodModel: MethodModel) {
        if (methodModel.parameters.isNotEmpty() || methodModel.doc.length > 3 || !methodModel.returnType.isVoid) {
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
        writeMethod(structureModel, methodModel)
    }

    override fun writeCallback(structureModel: StructureModel, methodModel: MethodModel, isSignal: Boolean) {
        writeSignal(structureModel, methodModel)
    }

    override fun writeUnsupported(model: Model) {}
    override fun writeEnd() {}
    override fun writeGetTypeFunction(structureModel: StructureModel) {}
    override fun writeMallocConstructor(structureModel: StructureModel) {}
    override fun writeStart(structureModel: StructureModel, namespaceModel: NamespaceModel) {}
    override fun writeInternalConstructor(structureModel: StructureModel) {}
    override fun writeBeginStruct(structureModel : StructureModel, fields: ModelList<ParameterModel>) {}
    override fun writeEndStruct() {}
    override fun writeBeginInstace(namespaceModel: NamespaceModel) {}
    override fun writeEndInstance() {}
}
