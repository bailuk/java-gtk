package ch.bailu.gtk

import ch.bailu.gtk.model.NamespaceModel
import ch.bailu.gtk.model.StructureModel
import java.io.*

class Directories(args: Array<String>) {
    private val javaBaseDir: File

    private val girDirs = ArrayList<GirDirectory>()

    init {
        addGirDirectoryIfExists(GirDirectory(File(Configuration.GIR_DIR_CUSTOM), "custom"))
        addGirDirectoryIfExists(GirDirectory(File(Configuration.GIR_DIR_LOCAL), "local"))

        var i = 0
        var jdir: File? = null
        while (i < args.size - 1) {
            if ("-j" == args[i]) {
                jdir = File(args[++i])
                jdir.mkdirs()
            } else if ("-i" == args[i]) {
                addGirDirectoryIfExists(GirDirectory(File(args[++i]), "external"))
            }
            i++
        }
        if (jdir is File) {
            javaBaseDir = jdir
        } else {
            println("Usage: call -j <java sources output directory> [-i <introspective directory>]\n")
            throw RuntimeException("Missing parameter")
        }
    }

    private fun addGirDirectoryIfExists(directory: GirDirectory) {
        if (directory.path.isDirectory) {
            girDirs.add(directory)
        } else {
            println("WARNING: Directory '${directory.path}' of type '${directory.type} 'does not exist\n")
        }
    }

    @Throws(IOException::class)
    fun getJavaWriter(className: String, nameSpaceModel: NamespaceModel): Writer {
        return BufferedWriter(FileWriter(getJavaFile(nameSpaceModel, className)))
    }

    @Throws(IOException::class)
    fun getPackageWriter(nameSpaceModel: NamespaceModel): Writer {
        return BufferedWriter(FileWriter(getPackageFile(nameSpaceModel)))
    }

    private fun getPackageFile(nameSpaceModel: NamespaceModel): File {
        val directory = File(javaBaseDir, nameSpaceModel.namespace)
        return File(createDirectory(directory), "package-info.java")
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
        girDirs.forEach {
            val girFile = File(it.path, girFileName)
            if (girFile.exists()) {
                println("  --> ${it.type}: $girFile")
                return girFile
            }
        }
        throw IOException("GIR file '$girFileName' not found")
    }
}

private data class GirDirectory(val path: File, val type: String)
