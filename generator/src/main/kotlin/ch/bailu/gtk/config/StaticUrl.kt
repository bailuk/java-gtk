package ch.bailu.gtk.config

import ch.bailu.gtk.model.StructureModel

class StaticUrl(val url: String): DocUrl() {
    override fun getUrl(structureModel: StructureModel): String {
        return url
    }
}