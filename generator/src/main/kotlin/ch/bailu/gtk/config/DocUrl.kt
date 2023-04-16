package ch.bailu.gtk.config

import ch.bailu.gtk.model.StructureModel

abstract class DocUrl {
    abstract fun getUrl(structureModel: StructureModel): String
    abstract fun getBaseUrl(): String
}
