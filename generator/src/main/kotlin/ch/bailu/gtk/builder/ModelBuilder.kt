package ch.bailu.gtk.builder

import ch.bailu.gtk.Configuration
import ch.bailu.gtk.model.StructureModel
import ch.bailu.gtk.model.NamespaceModel
import ch.bailu.gtk.parser.tag.*
import ch.bailu.gtk.config.NamespaceConfig
import ch.bailu.gtk.writer.*
import ch.bailu.gtk.writer.java.JavaApiWriter
import ch.bailu.gtk.writer.java.JavaImpWriter
import ch.bailu.gtk.writer.java.JavaJnaApiWriter
import ch.bailu.gtk.writer.java.JavaJnaWriter
import java.io.IOException
import java.io.Writer

class ModelBuilder : BuilderInterface {

    private var namespace: NamespaceModel = NamespaceModel()
    private var errorStubs: Boolean = false

    override fun buildStructure(structure: StructureTag) {
        val model = StructureModel(structure, namespace)

        if (model.isSupported) {
            writeJavaFile(model)
            if (model.hasNativeCalls()) {
                //writeCFile(model)
                //writeJavaImpFile(model)
                writeJavaJnaFile(model)
            }
        }
    }

    @Throws(IOException::class)
    private fun writeJavaImpFile(model: StructureModel) {
        var out: Writer? = null
        try {
            out = getJavaImpWriter(model, namespace)
            model.write(JavaImpWriter(TextWriter(out)))
        } finally {
            out?.close()
        }
    }

    @Throws(IOException::class)
    private fun writeCFile(model: StructureModel) {
        var out: Writer? = null
        try {
            out = getCWriter(model, namespace)
            if (!errorStubs)
			    model.write(CWriter(TextWriter(out)))
        } finally {
            out?.close()
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
            //writeCFile(model)
            //writeJavaImpFile(model)
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


    override fun buildErrorStubs(enabled: Boolean) {
        this.errorStubs = enabled
    }
}
