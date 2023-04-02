package ch.bailu.gtk.model

import ch.bailu.gtk.Configuration
import ch.bailu.gtk.NamespaceConfig
import ch.bailu.gtk.model.type.NamespaceType
import ch.bailu.gtk.parser.tag.MethodTag
import ch.bailu.gtk.parser.tag.NamespaceTag
import ch.bailu.gtk.parser.tag.ParameterTag
import ch.bailu.gtk.table.NamespaceTable

class NamespaceModel(
    val namespace: String = "",
    val library: String = "",
    val functions: List<MethodTag> = ArrayList(),
    val constants: List<ParameterTag> = ArrayList(),
    val infoDocUrl: String = "",
    val infoGirFile: String = "",
    val infoNamespace: String = "",
    val infoVersion: String = "",
    val infoSharedLibrary: String = ""
) : Model() {


    constructor(tag: NamespaceTag, config: NamespaceConfig) :
            this(
                namespace = tag.getName().lowercase(),
                library = config.library,
                functions = tag.getFunctions(),
                constants = tag.getConstants(),
                infoDocUrl = config.docUrl.getBaseUrl(),
                infoVersion = tag.version,
                infoNamespace = tag.getName(),
                infoGirFile = config.girFile,
                infoSharedLibrary = tag.sharedLibrary
            ) {
        setSupported("unknown-namespace", NamespaceTable.contains(namespace))
    }

    constructor(type: NamespaceType) : this(type.namespace) {
        setSupported("unknown-namespace", NamespaceTable.contains(namespace))
    }

    val fullNamespace: String
        get() = if ("" == namespace) {
            Configuration.BASE_NAME_SPACE_NODOT
        } else {
            Configuration.BASE_NAME_SPACE_DOT + namespace
        }

}
