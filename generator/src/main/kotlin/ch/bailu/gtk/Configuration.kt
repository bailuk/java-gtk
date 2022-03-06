package ch.bailu.gtk

import ch.bailu.gtk.config.GtkDocUrl
import ch.bailu.gtk.config.NamespaceConfig
import ch.bailu.gtk.config.StaticUrl
import ch.bailu.gtk.writer.java_doc.JavaDoc
import ch.bailu.gtk.writer.java_doc.JavaDocHtml
import java.io.File
import java.io.Writer

class Configuration @Throws(RuntimeException::class) private constructor(args: Array<String>) {

    companion object {
        const val BASE_NAME_SPACE_DOT = "ch.bailu.gtk."
        const val BASE_NAME_SPACE_NODOT = "ch.bailu.gtk"
        const val HEADER_FILE_BASE = "ch_bailu_gtk_"
        const val JNI_METHOD_NAME_BASE = "Java_ch_bailu_gtk_"

        const val GIR_DIR_CUSTOM = "src/main/resources"
        const val GIR_DIR_LOCAL = "src/main/resources/gir"

        const val LOG_STRUCTURE_TABLE_FILE = "build/structure_table.out"
        const val LOG_ALIAS_TABLE_FILE = "build/alias_table.out"
        const val LOG_CALLBACK_TABLE_FILE = "build/callback_table.out"

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



        private var INSTANCE: Configuration? = null

        fun getInstance(): Configuration {
            val instance = INSTANCE
            if (instance != null) {
                return instance
            } else {
                throw java.lang.RuntimeException("Configuration not initialized")
            }
        }

        fun init(args: Array<String>) {
            INSTANCE = Configuration(args)
        }

        fun createJavaDocConfig(writer: Writer): JavaDoc {
            return JavaDocHtml(writer)
        }

    }
    val javaBaseDir: File
    val cBaseDir: File
    val girBaseDir: File


    init {
        var i = 0
        var cdir: File? = null
        var jdir: File? = null
        var gdir: File? = null
        while (i < args.size - 1) {
            if ("-c" == args[i]) {
                cdir = getDirectory(args[++i], true)
            } else if ("-j" == args[i]) {
                jdir = getDirectory(args[++i], true)
            } else if ("-i" == args[i]) {
                gdir = getDirectory(args[++i], false)
            }
            i++
        }
        if (cdir != null && jdir != null && gdir != null) {
            cBaseDir = cdir
            javaBaseDir = jdir
            girBaseDir = gdir
        } else {
            println("Usage: call -i <introspective directory> -c <c sources output directory> -j <java sources output directory>\n")
            throw RuntimeException("Missing parameter")
        }
    }

    private fun getDirectory(dir: String, create: Boolean): File {
        val result = File(dir)
        if (create) {
            result.mkdirs()
            if (!result.exists()) {
                throw RuntimeException("$dir does not exist.")
            }
        }
        return result
    }
}
