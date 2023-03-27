package ch.bailu.gtk.writer.java

import ch.bailu.gtk.model.NamespaceModel
import ch.bailu.gtk.writer.Names
import ch.bailu.gtk.writer.TextWriter
import ch.bailu.gtk.writer.java_doc.JavaDoc

class PackageWriter(private val out: TextWriter, val doc: JavaDoc) {

    fun write(namespaceModel: NamespaceModel) {
        writeDoc(namespaceModel)
        out.a("package ")
        out.a(namespaceModel.fullNamespace)
        out.a(";\n")
    }

    private fun writeDoc(namespaceModel: NamespaceModel) {
            doc.writeStart(0)
            doc.writeBlockPlain(
                    """
                    Generated: ${namespaceModel.infoNamespace} ${namespaceModel.infoVersion}
                    <br>API Reference Manual: <a href="${namespaceModel.infoDocUrl}">${namespaceModel.infoDocUrl}</a>}
                    <br>Functions class: {@link ${namespaceModel.fullNamespace}.${Names.getJavaClassName(namespaceModel.namespace)}}
                    <br>Constants class: {@link ${namespaceModel.fullNamespace}.${Names.getJavaPackageConstantsInterfaceName(namespaceModel.namespace)}}
                    <br>Generated from: ${namespaceModel.infoGirFile}
                    <br>Shared library: ${namespaceModel.infoSharedLibrary}
                    <br>Configured library: ${namespaceModel.library}
                    """.trimIndent())
        doc.writeDocEnd()
    }
}
