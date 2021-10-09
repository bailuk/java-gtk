package ch.bailu.gtk.builder

import ch.bailu.gtk.model.StructureModel
import ch.bailu.gtk.model.NamespaceModel
import ch.bailu.gtk.parser.tag.*
import ch.bailu.gtk.writer.*
import java.io.IOException
import java.io.Writer

class ModelBuilder : BuilderInterface {

    private var namespace: NamespaceModel = NamespaceModel()

    override fun buildStructure(structure: StructureTag) {
        val model = StructureModel(structure, namespace)
        writeJavaFile(model)
        if (model.hasNativeCalls()) {
            writeCFile(model)
            writeJavaImpFile(model)
        }
    }

    @Throws(IOException::class)
    private fun writeJavaImpFile(model: StructureModel) {
        var out: Writer? = null
        try {
            out = getJavaImpWriter(model, namespace)
            model.write(JavaImpWriter(out))
        } finally {
            out?.close()
        }
    }

    @Throws(IOException::class)
    private fun writeCFile(model: StructureModel) {
        var out: Writer? = null
        try {
            out = getCWriter(model, namespace)
            model.write(CWriter(out))
        } finally {
            out?.close()
        }
    }


    override fun buildNamespaceStart(namespace: NamespaceTag) {
        this.namespace = NamespaceModel(namespace)
    }

    @Throws(IOException::class)
    override fun buildNamespaceEnd(namespace: NamespaceTag) {
        // functions
        var model = StructureModel(namespace)
        writeJavaFile(model)
        if (model.hasNativeCalls()) {
            writeCFile(model)
            writeJavaImpFile(model)
        }


        // constants
        model = StructureModel(NamespaceModel(namespace), namespace.getConstants())
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
            model.write(JavaApiWriter(out))
        } finally {
            out?.close()
        }
    }
}