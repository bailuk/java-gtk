package ch.bailu.gtk.config

import ch.bailu.gtk.model.StructureModel
import ch.bailu.gtk.model.type.StructureType

class GtkDocUrl(private val baseUrl: String, private val section: String): DocUrl() {

    override fun getUrl(structureModel: StructureModel): String {
        val type = getType(structureModel.structureType)

        return if ("" == type) {
            baseUrl
        } else {
             "${baseUrl}${section}/${type}.${structureModel.apiName}.html"
        }
    }

    override fun getBaseUrl(): String {
        return baseUrl
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
