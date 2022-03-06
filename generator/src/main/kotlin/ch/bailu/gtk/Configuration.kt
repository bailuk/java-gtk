package ch.bailu.gtk

import ch.bailu.gtk.config.DocUrl
import ch.bailu.gtk.config.GtkDocUrl
import ch.bailu.gtk.config.StaticUrl
import ch.bailu.gtk.writer.java_doc.JavaDoc
import ch.bailu.gtk.writer.java_doc.JavaDocHtml
import java.io.Writer

object Configuration  {

    const val BASE_NAME_SPACE_NODOT    = "ch.bailu.gtk"
    const val BASE_NAME_SPACE_DOT      = "ch.bailu.gtk."
    const val HEADER_FILE_BASE         = "ch_bailu_gtk_"

    const val GIR_DIR_CUSTOM           = "src/main/resources"
    const val GIR_DIR_LOCAL            = "src/main/resources/gir"

    const val LOG_STRUCTURE_TABLE_FILE = "build/structure_table.out"
    const val LOG_ALIAS_TABLE_FILE     = "build/alias_table.out"
    const val LOG_CALLBACK_TABLE_FILE  = "build/callback_table.out"

    val NAMESPACES = arrayOf(
                NamespaceConfig("GObject-2.0.gir",    "gobject-2.0",    GtkDocUrl("gobject")),
                NamespaceConfig("Gtk-4.0.gir",        "gtk-4",          GtkDocUrl("gtk4")),
                NamespaceConfig("Gio-2.0.gir",        "gio-2.0",        GtkDocUrl("gio")),
                NamespaceConfig("Gdk-4.0.gir",        "gtk-4",          GtkDocUrl("gdk4")),
                NamespaceConfig("PangoCairo-1.0.gir", "pangocairo-1.0", GtkDocUrl("PangoCairo")),
                NamespaceConfig("cairo-custom.gir",   "cairo",          StaticUrl("https://www.cairographics.org/manual/")),
                NamespaceConfig("GLib-2.0.gir",       "glib-2.0",       GtkDocUrl("glib")),
                NamespaceConfig("Pango-1.0.gir",      "pango-1.0",      GtkDocUrl("Pango")),
                NamespaceConfig("GdkPixbuf-2.0.gir",  "gdk_pixbuf-2.0", GtkDocUrl("gdk-pixbuf")),
                NamespaceConfig("Adw-1.gir",          "libadwaita-1",   StaticUrl("https://gnome.pages.gitlab.gnome.org/libadwaita/doc/")))


    fun createJavaDocConfig(out: Writer): JavaDoc {
        return JavaDocHtml(out)

    }
}

data class NamespaceConfig(val girFile: String, val library: String, val docUrl: DocUrl)