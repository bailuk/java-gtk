package ch.bailu.gtk.writer.java_doc

import ch.bailu.gtk.model.*
import ch.bailu.gtk.model.filter.ModelList
import ch.bailu.gtk.writer.CodeWriter
import ch.bailu.gtk.writer.Names
import ch.bailu.gtk.writer.TextWriter


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
        doc.writeStart(4)

        val block = """
            Connect to signal "${methodModel.name}".
            <br>See {@link ${Names.getJavaCallbackInterfaceName(methodModel.name)}#${Names.getJavaCallbackMethodName(methodModel.name)}} for signal description.
            <br>Field {@link #${methodModel.signalNameVariable}} contains original signal name and can be used as resource reference.
            <br>
            @param signal callback function (lambda).
            @returns {@link ch.bailu.gtk.lib.handler.SignalHandler}. Can be used to disconnect signal and to release callback function.
        """.trimIndent()

        doc.writeBlockPlain(block)
        doc.writeDocEnd()
    }


    override fun writeField(structureModel: StructureModel, parameterModel: ParameterModel) {
        //writeConstant(structureModel, parameterModel)
    }

    override fun writeFunction(structureModel: StructureModel, methodModel: MethodModel) {
        writeMethod(structureModel, methodModel)
    }

    override fun writeCallback(structureModel: StructureModel, methodModel: MethodModel, isSignal: Boolean) {
        if (methodModel.parameters.isNotEmpty() || methodModel.doc.length > 3 || !methodModel.returnType.isVoid) {
            doc.writeStart(8)
            doc.writeBlock(methodModel.doc)
            doc.writeParameter(methodModel)
            doc.writeReturn(methodModel)
            doc.writeDocEnd()
        }
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
