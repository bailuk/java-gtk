package ch.bailu.gtk.builder

import ch.bailu.gtk.model.ClassModel
import ch.bailu.gtk.model.NamespaceModel
import ch.bailu.gtk.tag.*
import ch.bailu.gtk.writer.CWriter
import ch.bailu.gtk.writer.JavaApiWriter
import ch.bailu.gtk.writer.JavaImpWriter
import java.io.IOException
import java.io.Writer

class ModelBuilder : BuilderInterface {

    private var namespace: NamespaceModel = NamespaceModel()

    override fun buildStructure(structure: StructureTag) {
        val model = ClassModel(structure, namespace)
        writeJavaFile(model)
        if (model.hasNativeCalls()) {
            writeCFile(model)
            writeJavaImpFile(model)
        }
    }

    @Throws(IOException::class)
    private fun writeJavaImpFile(model: ClassModel) {
        var out: Writer? = null
        try {
            out = IO.getJavaImpWriter(model, namespace)
            model.write(JavaImpWriter(out))
        } finally {
            IO.close(out)
        }
    }

    @Throws(IOException::class)
    private fun writeCFile(model: ClassModel) {
        var out: Writer? = null
        try {
            out = IO.getCWriter(model, namespace)
            model.write(CWriter(out))
        } finally {
            IO.close(out)
        }
    }


    override fun buildNamespaceStart(namespace: NamespaceTag) {
        this.namespace = NamespaceModel(namespace)
    }

    @Throws(IOException::class)
    override fun buildNamespaceEnd(namespace: NamespaceTag) {
        // functions
        var model = ClassModel(namespace)
        writeJavaFile(model)
        if (model.hasNativeCalls()) {
            writeCFile(model)
            writeJavaImpFile(model)
        }


        // constants
        model = ClassModel(NamespaceModel(namespace), namespace.getConstants())
        writeJavaFile(model)
    }


    override fun buildAlias(alias: AliasTag) {}


    @Throws(IOException::class)
    override fun buildEnumeration(enumeration: EnumerationTag) {
        val model = ClassModel(namespace, enumeration)
        writeJavaFile(model)
    }

    override fun buildCallback(callbackTag: CallbackTag) {}

    @Throws(IOException::class)
    private fun writeJavaFile(model: ClassModel) {
        var out: Writer? = null
        try {
            out = IO.getJavaWriter(model.apiName, namespace)
            model.write(JavaApiWriter(out))
        } finally {
            IO.close(out)
        }
    }
}