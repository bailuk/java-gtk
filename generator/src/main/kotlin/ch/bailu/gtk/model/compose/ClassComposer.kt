package ch.bailu.gtk.model.compose

import ch.bailu.gtk.model.NamespaceModel
import ch.bailu.gtk.model.StructureModel
import ch.bailu.gtk.model.filter.filterCreateMallocConstructor
import ch.bailu.gtk.model.list.ModelLists
import ch.bailu.gtk.writer.CodeWriter

class ClassComposer : CodeComposer() {

    override fun compose(writer: CodeWriter, namespaceModel: NamespaceModel, structureModel: StructureModel, models: ModelLists) {

        writer.writeClass(structureModel, namespaceModel)

        for (cb in models.callbacks) {
            writer.writeCallback(structureModel, cb)
        }

        for (m in models.privateFactories) {
            writer.writePrivateFactory(structureModel, m)
        }

        for (m in models.factories) {
            writer.writeFactory(structureModel, m)
        }

        writer.writeInternalConstructor(structureModel)

        if (structureModel.isRecord && filterCreateMallocConstructor(structureModel)) {
            writer.writeMallocConstructor(structureModel)
        }

        for (m in models.constructors) {
            writer.writeConstructor(structureModel, m)
        }

        for (p in models.fields) {
            writer.writeField(structureModel, p)
        }

        for (m in models.methods) {
            writer.writeNativeMethod(structureModel, m)
        }

        for (s in models.signals) {
            writer.writeSignal(structureModel, s)
        }

        for (m in models.functions) {
            writer.writeFunction(structureModel, m)
        }

        if (structureModel.hasGetTypeFunction) {
            writer.writeGetTypeFunction(structureModel)
        }
    }
}