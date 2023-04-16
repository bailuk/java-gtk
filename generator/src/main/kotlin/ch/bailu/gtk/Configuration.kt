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

    const val GIR_DIR_CUSTOM           = "src/main/resources"
    const val GIR_DIR_LOCAL            = "src/main/resources/gir"

    const val LOG_STRUCTURE_TABLE_FILE = "build/structure_table.out"
    const val LOG_ALIAS_TABLE_FILE     = "build/alias_table.out"
    const val LOG_CALLBACK_TABLE_FILE  = "build/callback_table.out"
    const val LOG_ENUM_TABLE_FILE      = "build/enum_table.out"

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
                NamespaceConfig("Adw-1.gir",          "adwaita-1",      GtkDocUrl("https://gnome.pages.gitlab.gnome.org/libadwaita/doc/", "main")))

    fun createJavaDocConfig(out: Writer): JavaDoc {
        return JavaDocHtml(out)
    }
}

data class NamespaceConfig(val girFile: String, val library: String, val docUrl: DocUrl)
