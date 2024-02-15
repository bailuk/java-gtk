package ch.bailu.gtk

import ch.bailu.gtk.config.DocUrl
import ch.bailu.gtk.config.GtkDocUrl
import ch.bailu.gtk.config.StaticUrl
import ch.bailu.gtk.log.Logable
import ch.bailu.gtk.writer.java_doc.JavaDoc
import ch.bailu.gtk.writer.java_doc.JavaDocHtml
import java.io.Writer

object Configuration : Logable {

    const val BASE_NAME_SPACE_NODOT    = "ch.bailu.gtk"
    const val BASE_NAME_SPACE_DOT      = "ch.bailu.gtk."

    const val GIR_DIR_CUSTOM           = "src/main/resources"
    const val GIR_DIR_LOCAL            = "src/main/resources/gir"

    const val LOG_DEFAULT_DIRECTORY    = "build"
    const val LOG_STRUCTURE_TABLE_FILE = "structure_table.md"
    const val LOG_ALIAS_TABLE_FILE     = "alias_table.md"
    const val LOG_CALLBACK_TABLE_FILE  = "callback_table.md"
    const val LOG_ENUM_TABLE_FILE      = "enum_table.md"
    const val LOG_SIZE_TABLE_FILE      = "size_table.md"

    const val LOG_CONFIGURATION        = "README.md"

    const val DOCS_GTK_ORG = "https://docs.gtk.org/"

    val NAMESPACES = arrayOf(
                NamespaceConfig("GObject-2.0.gir",    "gobject-2.0",    GtkDocUrl(DOCS_GTK_ORG, "gobject")),
                NamespaceConfig("Gtk-4.0.gir",        "gtk-4",          GtkDocUrl(DOCS_GTK_ORG, "gtk4")),
                NamespaceConfig("Gio-2.0.gir",        "gio-2.0",        GtkDocUrl(DOCS_GTK_ORG, "gio")),
                NamespaceConfig("Gdk-4.0.gir",        "gtk-4",          GtkDocUrl(DOCS_GTK_ORG, "gdk4")),
                NamespaceConfig("Gsk-4.0.gir",        "gtk-4",          GtkDocUrl(DOCS_GTK_ORG, "gsk4")),
                NamespaceConfig("Graphene-1.0.gir",   "graphene-1.0",   StaticUrl("https://ebassi.github.io/graphene/docs/")),
                NamespaceConfig("PangoCairo-1.0.gir", "pangocairo-1.0", GtkDocUrl(DOCS_GTK_ORG, "PangoCairo")),
                NamespaceConfig("cairo-custom.gir",   "cairo",          StaticUrl("https://www.cairographics.org/manual/")),
                NamespaceConfig("GLib-2.0.gir",       "glib-2.0",       GtkDocUrl(DOCS_GTK_ORG, "glib")),
                NamespaceConfig("Pango-1.0.gir",      "pango-1.0",      GtkDocUrl(DOCS_GTK_ORG, "Pango")),
                NamespaceConfig("GdkPixbuf-2.0.gir",  "gdk_pixbuf-2.0", GtkDocUrl(DOCS_GTK_ORG, "gdk-pixbuf")),
                NamespaceConfig("Geoclue-2.0.gir",    "geoclue-2",      StaticUrl("https://www.freedesktop.org/software/geoclue/docs/libgeoclue/")),
                NamespaceConfig("Adw-1.gir",          "adwaita-1",      GtkDocUrl("https://gnome.pages.gitlab.gnome.org/libadwaita/doc/", "main")),
                NamespaceConfig("Gst-1.0.gir",        "gstreamer-1.0",  StaticUrl("https://gstreamer.freedesktop.org/documentation/gstreamer/gi-index.html"))
    )

    fun createJavaDocConfig(out: Writer): JavaDoc {
        return JavaDocHtml(out)
    }

    override fun log(writer: Writer) {
        writer.write("# ${javaClass.name}\n\n")

        writer.write("## Namespaces\n\n")
        writer.write("| GIR File | Library | Documentation\n")
        writer.write("|----------|---------|--------------\n")


        NAMESPACES.forEach {
            writer.write(String.format("| %s | %s | %s\n", it.girFile, it.girFile, toUrl(it.docUrl.getBaseUrl())))
        }

        writer.write("\n## Tables\n\n")
        writer.write("- ${toUrl(LOG_STRUCTURE_TABLE_FILE)}\n")
        writer.write("- ${toUrl(LOG_ALIAS_TABLE_FILE)}\n")
        writer.write("- ${toUrl(LOG_CALLBACK_TABLE_FILE)}\n")
        writer.write("- ${toUrl(LOG_ENUM_TABLE_FILE)}\n")
        writer.write("- ${toUrl(LOG_SIZE_TABLE_FILE)}\n")
    }

    private fun toUrl(url: String): String {
        return "[${url}](${url})"
    }
}

data class NamespaceConfig(val girFile: String, val library: String, val docUrl: DocUrl)
