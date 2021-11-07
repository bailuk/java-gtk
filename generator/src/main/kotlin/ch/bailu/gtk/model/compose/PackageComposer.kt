package ch.bailu.gtk.model.compose

import ch.bailu.gtk.model.StructureModel
import ch.bailu.gtk.model.list.ModelLists
import ch.bailu.gtk.writer.CodeWriter

class PackageComposer : CodeComposer() {

    override fun compose(writer: CodeWriter, structureModel: StructureModel, models: ModelLists) {
        writer.writeClass(structureModel)
        for (cb in models.callbacks) {
            writer.writeCallback(structureModel, cb)
        }
        for (m in models.functions) {
            writer.writeFunction(structureModel, m)
        }
    }
}