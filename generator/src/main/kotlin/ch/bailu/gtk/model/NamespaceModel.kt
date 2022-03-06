package ch.bailu.gtk.model

import ch.bailu.gtk.Configuration
import ch.bailu.gtk.NamespaceConfig
import ch.bailu.gtk.converter.RelativeNamespaceType
import ch.bailu.gtk.parser.tag.MethodTag
import ch.bailu.gtk.parser.tag.NamespaceTag
import ch.bailu.gtk.parser.tag.ParameterTag
import ch.bailu.gtk.table.NamespaceTable

class NamespaceModel(
    val namespace: String = "",
    val namespaceConfig: NamespaceConfig = Configuration.NAMESPACES[0],
    val functions: List<MethodTag> = ArrayList(),
    val constants: List<ParameterTag> = ArrayList()
) : Model() {

    val includes: MutableList<String> = ArrayList()

    constructor(tag: NamespaceTag, config: NamespaceConfig) :
            this(tag.getName().lowercase(), config, tag.getFunctions(), tag.getConstants()) {

        for (i in tag.getIncludes()) {
            includes.add(i.getName())
        }
        setSupported("Namespace", NamespaceTable.contains(namespace))
    }

    constructor(type: RelativeNamespaceType) : this(type.namespace) {
        setSupported("Namespace", NamespaceTable.contains(namespace))
    }

    val fullNamespace: String
        get() = if ("" == namespace) {
            Configuration.BASE_NAME_SPACE_NODOT
        } else {
            Configuration.BASE_NAME_SPACE_DOT + namespace
        }

}
