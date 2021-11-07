package ch.bailu.gtk.model.compose

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
    abstract fun compose(writer: CodeWriter, structureModel: StructureModel, models: ModelLists)

    fun write(writer: CodeWriter, structureModel: StructureModel, models: ModelLists) {
        writeStart(writer, structureModel)
        compose(writer, structureModel, models)
        writeEnd(writer, models)
    }

    fun writeStart(writer: CodeWriter, structureModel: StructureModel) {
        writer.writeStart(structureModel, structureModel.nameSpaceModel)
    }

    fun writeEnd(writer: CodeWriter, models: ModelLists) {
        for (m in models.unsupported) {
            writer.writeUnsupported(m)
        }
        writer.writeEnd()
    }
}