package ch.bailu.gtk.builder

import ch.bailu.gtk.Configuration
import ch.bailu.gtk.model.StructureModel
import ch.bailu.gtk.model.NamespaceModel
import ch.bailu.gtk.parser.tag.*
import ch.bailu.gtk.config.NamespaceConfig
import ch.bailu.gtk.writer.*
import ch.bailu.gtk.writer.java.JavaJnaApiWriter
import ch.bailu.gtk.writer.java.JavaJnaWriter
import java.io.IOException
import java.io.Writer

class ModelBuilder : BuilderInterface {

    private var namespace: NamespaceModel = NamespaceModel()

    override fun buildStructure(structure: StructureTag) {
        val model = StructureModel(structure, namespace)

        if (model.isSupported) {
            writeJavaFile(model)
            if (model.hasNativeCalls()) {
                writeJavaJnaFile(model)
            }
        }
    }


    override fun buildNamespaceStart(namespace: NamespaceTag, namespaceConfig: NamespaceConfig) {
        this.namespace = NamespaceModel(namespace, namespaceConfig)
    }

    @Throws(IOException::class)
    override fun buildNamespaceEnd() {
        // functions
        var model = StructureModel(namespace)
        writeJavaFile(model)
        if (model.hasNativeCalls()) {
            writeJavaJnaFile(model)
        }


        // constants
        model = StructureModel(this.namespace, namespace.constants)
        writeJavaFile(model)
    }


    override fun buildAlias(aliasTag: AliasTag) {}


    @Throws(IOException::class)
    override fun buildEnumeration(enumeration: EnumerationTag) {
        val model = StructureModel(namespace, enumeration)
        writeJavaFile(model)
    }

    override fun buildCallback(callbackTag: CallbackTag) {}

    @Throws(IOException::class)
    private fun writeJavaFile(model: StructureModel) {
        var out: Writer? = null
        try {
            out = getJavaWriter(model.apiName, namespace)
            model.write(JavaJnaApiWriter(TextWriter(out), Configuration.createJavaDocConfig(out)))
        } finally {
            out?.close()
        }
    }

    @Throws(IOException::class)
    private fun writeJavaJnaFile(model: StructureModel) {
        var out: Writer? = null
        try {
            out = getJavaJnaWriter(model, namespace)
            model.write(JavaJnaWriter(TextWriter(out)))
        } finally {
            out?.close()
        }
    }
}
