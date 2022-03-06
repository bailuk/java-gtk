package ch.bailu.gtk.config

import ch.bailu.gtk.model.StructureModel
import ch.bailu.gtk.model.type.StructureType

class GtkDocUrl(val section: String): DocUrl() {

    companion object {
        const val baseUrl = "https://docs.gtk.org/"
    }
    override fun getUrl(structureModel: StructureModel): String {
        val name = structureModel.apiName
        val type = getType(structureModel.structureType)

        return if ("" == type) {
            baseUrl
        } else {
            "${baseUrl}${section}/${type}.${name}.html"
        }
    }

    private fun getType(structureType: StructureType): String {
        return when {
            structureType.compare(StructureType.Types.RECORD) -> {
                "struct"
            }
            structureType.compare(StructureType.Types.CLASS) -> {
                "class"
            }
            structureType.compare(StructureType.Types.INTERFACE) -> {
                "iface"
            }
            structureType.compare(StructureType.Types.CALLBACK) -> {
                "callback"
            }
            else -> {
                ""
            }
        }
    }
}