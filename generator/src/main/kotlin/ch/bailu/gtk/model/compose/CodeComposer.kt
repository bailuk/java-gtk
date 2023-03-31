package ch.bailu.gtk.model.compose

import ch.bailu.gtk.model.NamespaceModel
import ch.bailu.gtk.model.StructureModel
import ch.bailu.gtk.model.list.ModelLists
import ch.bailu.gtk.model.type.StructureType
import ch.bailu.gtk.writer.CodeWriter

abstract class CodeComposer {

    companion object {
        fun factory(structureType: StructureType): CodeComposer {
            return if (structureType.isPackage) {
                PackageComposer()
            } else if (structureType.isClassType) {
                ClassComposer()
            } else {
                InterfaceComposer()
            }
        }
    }
    abstract fun compose(writer: CodeWriter, namespaceModel: NamespaceModel, structureModel: StructureModel, models: ModelLists)

    fun write(writer: CodeWriter, namespaceModel: NamespaceModel, structureModel: StructureModel, models: ModelLists) {
        writeStart(writer, structureModel)
        compose(writer, namespaceModel, structureModel, models)
        writeEnd(writer, structureModel, models)
    }

    private fun writeStart(writer: CodeWriter, structureModel: StructureModel) {
        writer.writeStart(structureModel, structureModel.nameSpaceModel)
    }

    fun writeEnd(writer: CodeWriter, structureModel: StructureModel, models: ModelLists) {
        writer.writeClassEnd()
        writer.writeDebugBegin(structureModel)
        for (m in models.unsupported) {
            writer.writeDebugUnsupported(m)
        }
        writer.writeDebugEnd()
    }
}
