package ch.bailu.gtk.model.compose

import ch.bailu.gtk.model.StructureModel
import ch.bailu.gtk.model.filter.filterCreateMallocConstructor
import ch.bailu.gtk.model.list.ModelLists
import ch.bailu.gtk.writer.CodeWriter

class ClassComposer : CodeComposer() {

    override fun compose(writer: CodeWriter, structureModel: StructureModel, models: ModelLists) {

        writer.writeClass(structureModel)
        writer.next()

        for (cb in models.callbacks) {
            writer.writeCallback(structureModel, cb)
        }
        writer.next()

        for (m in models.privateFactories) {
            writer.writePrivateFactory(structureModel, m)
        }
        writer.next()

        for (m in models.factories) {
            writer.writeFactory(structureModel, m)
        }
        writer.next()

        writer.writeInternalConstructor(structureModel)
        writer.next()

        if (structureModel.isRecord && filterCreateMallocConstructor(structureModel)) {
            writer.writeMallocConstructor(structureModel)
        }
        writer.next()

        for (m in models.constructors) {
            writer.writeConstructor(structureModel, m)
        }
        writer.next()

        for (p in models.fields) {
            writer.writeField(structureModel, p)
        }
        writer.next()

        for (m in models.methods) {
            writer.writeNativeMethod(structureModel, m)
        }
        writer.next()

        for (s in models.signals) {
            writer.writeSignal(structureModel, s)
        }
        writer.next()

        for (m in models.functions) {
            writer.writeFunction(structureModel, m)
        }
    }
}