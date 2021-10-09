package ch.bailu.gtk

import java.io.File

class Configuration {

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

        val GIR_FILES = arrayOf(
                    "GObject-2.0.gir",
                    "Gtk-3.0.gir",
                    "Gio-2.0.gir",
                    "Gdk-3.0.gir",
                    "PangoCairo-1.0.gir",
                    "cairo-custom.gir",
                    "GLib-2.0.gir",
                    "Atk-1.0.gir",
                    "Pango-1.0.gir",
                    "GdkPixbuf-2.0.gir")



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

    }
    var javaBaseDir: File
    var cBaseDir: File
    var girBaseDir: File


    @Throws(RuntimeException::class)
    private constructor(args: Array<String>) {
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
                gdir = getDirectory(args[++i], true)
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
        }
        if (!result.exists()) {
            throw RuntimeException("$dir does not exist.")
        }
        return result
    }


    fun getJavaBaseDir(): String {
        return javaBaseDir.absolutePath
    }

    fun getCBaseDir(): String {
        return cBaseDir.absolutePath
    }
}