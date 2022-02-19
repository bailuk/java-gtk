package ch.bailu.gtk.model.compose

import ch.bailu.gtk.model.NamespaceModel
import ch.bailu.gtk.model.StructureModel
import ch.bailu.gtk.model.list.ModelLists
import ch.bailu.gtk.writer.CodeWriter

class InterfaceComposer : CodeComposer() {

    override fun compose(writer: CodeWriter, namespaceModel: NamespaceModel, structureModel: StructureModel, models: ModelLists) {
        writer.writeInterface(structureModel)
        for (p in models.constants) {
            writer.writeConstant(p)
        }
    }
}