package ch.bailu.gtk.writer

import ch.bailu.gtk.Configuration
import ch.bailu.gtk.model.MethodModel
import ch.bailu.gtk.model.StructureModel
import ch.bailu.gtk.model.NamespaceModel
import ch.bailu.gtk.model.ParameterModel
import java.io.*


@Throws(IOException::class)
fun getJavaWriter(className: String, nameSpaceModel: NamespaceModel): Writer {
    return BufferedWriter(FileWriter(getJavaFile(className, nameSpaceModel)))
}

@Throws(IOException::class)
private fun getJavaFile(className: String, nameSpaceModel: NamespaceModel): File {
    return File(createDirectory(nameSpaceModel.javaSourceDirectory), "$className.java")
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
fun getReader(file: File): Reader {
    return BufferedReader(InputStreamReader(FileInputStream(file)))
}

@Throws(IOException::class)
fun getCWriter(model: StructureModel, namespace: NamespaceModel): Writer {
    return BufferedWriter(FileWriter(getCFile(model.impName, namespace)))
}

@Throws(IOException::class)
private fun getCFile(name: String, namespace: NamespaceModel): File {
    val fn = StringBuilder()
    fn.append(Configuration.HEADER_FILE_BASE).append(namespace.namespace).append("_").append(name).append(".c")
    return File(createDirectory(namespace.cSourceDirectory), fn.toString())
}

@Throws(IOException::class)
fun getJavaImpWriter(structureModel: StructureModel, namespace: NamespaceModel): Writer {
    return BufferedWriter(FileWriter(getJavaFile(structureModel.impName, namespace)))
}

@Throws(IOException::class)
fun getJavaJnaWriter(structureModel: StructureModel, namespace: NamespaceModel): Writer {
    return BufferedWriter(FileWriter(getJavaFile(structureModel.jnaName, namespace)))
}


fun emitterIdFromModel(methodModel: MethodModel, toString: (ParameterModel)->String): String{
    return try {
        val last = methodModel.parameters.last { it.apiType == "ch.bailu.gtk.type.Pointer" }
        toString(last)
    } catch (e : NoSuchElementException) {
        "0"
    }
}
