package ch.bailu.gtk

import ch.bailu.gtk.model.NamespaceModel
import ch.bailu.gtk.model.StructureModel
import java.io.*

class Directories(args: Array<String>) {
    private val javaBaseDir: File
    private val girBaseDir: File


    init {
        var i = 0
        var jdir: File? = null
        var gdir: File? = null
        while (i < args.size - 1) {
            if ("-j" == args[i]) {
                jdir = getDirectory(args[++i], true)
            } else if ("-i" == args[i]) {
                gdir = getDirectory(args[++i], false)
            }
            i++
        }
        if (jdir != null && gdir != null) {
            javaBaseDir = jdir
            girBaseDir = gdir
        } else {
            println("Usage: call -i <introspective directory> -j <java sources output directory>\n")
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


    @Throws(IOException::class)
    fun getJavaWriter(className: String, nameSpaceModel: NamespaceModel): Writer {
        return BufferedWriter(FileWriter(getJavaFile(nameSpaceModel, className)))
    }

    @Throws(IOException::class)
    private fun getJavaFile(nameSpaceModel: NamespaceModel, className: String): File {
        val directory = File(javaBaseDir, nameSpaceModel.namespace)
        return File(createDirectory(directory), "$className.java")
    }

    @Throws(IOException::class)
    private fun createDirectory(directory: File): File {
        if (!directory.exists() && !directory.mkdirs()) {
            throw IOException("Could not create directory:" + directory.absolutePath)
        }
        return directory
    }

    @Throws(IOException::class)
    fun close(closeable: Closeable?) {
        closeable?.close()
    }

    @Throws(FileNotFoundException::class)
    fun getGirReader(girFileName: String): Reader {
        return BufferedReader(InputStreamReader(FileInputStream(getGirFile(girFileName))))
    }

    @Throws(IOException::class)
    fun getJavaJnaWriter(structureModel: StructureModel, namespace: NamespaceModel): Writer {
        return BufferedWriter(FileWriter(getJavaFile(namespace, structureModel.jnaName)))
    }

    private fun getGirFile(girFileName: String): File {
        var result = File(Configuration.GIR_DIR_CUSTOM, girFileName)
        var type = "custom"
        if (!result.exists()) {
            result = File(girBaseDir, girFileName)
            type = "system"
            if (!result.exists()) {
                result = File(Configuration.GIR_DIR_LOCAL, girFileName)
                type = "local"
                if (!result.exists()) {
                    throw IOException("File does not exist $result")
                }
            }
        }
        println("  --> ${type}: $result")
        return result
    }
}
