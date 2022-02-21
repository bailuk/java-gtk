package ch.bailu.gtk.model.compose

import ch.bailu.gtk.model.NamespaceModel
import ch.bailu.gtk.model.StructureModel
import ch.bailu.gtk.model.filter.filterCreateMallocConstructor
import ch.bailu.gtk.model.list.ModelLists
import ch.bailu.gtk.writer.CodeWriter

class ClassComposer : CodeComposer() {

    override fun compose(writer: CodeWriter, namespaceModel: NamespaceModel, structureModel: StructureModel, models: ModelLists) {

        writer.writeClass(structureModel)

        models.callbacks.forEach { writer.writeCallback(structureModel, it) }
        models.signals.forEach   { writer.writeCallback(structureModel, it, true) }

        writer.writeInternalConstructor(structureModel)

        if (structureModel.isRecord && filterCreateMallocConstructor(structureModel)) {
            writer.writeMallocConstructor(structureModel)
        }

        writer.writeBeginStruct()
        models.fields.forEach   { writer.writeField(structureModel, it) }
        writer.writeEndStruct()

        writer.writeBeginInstace(namespaceModel)
        models.privateFactories.forEach { writer.writePrivateFactory(structureModel, it) }
        models.factories.forEach        { writer.writeFactory(structureModel, it) }
        models.constructors.forEach     { writer.writeConstructor(structureModel, it) }
        models.methods.forEach          { writer.writeMethod(structureModel, it) }
        models.signals.forEach          { writer.writeSignal(structureModel, it) }
        models.functions.forEach       { writer.writeFunction(structureModel, it) }
        if (structureModel.hasGetTypeFunction) {
            writer.writeGetTypeFunction(structureModel)
        }
        writer.writeEndInstance()
    }
}