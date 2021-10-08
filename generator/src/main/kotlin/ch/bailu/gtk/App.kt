package ch.bailu.gtk

import ch.bailu.gtk.builder.AliasBuilder
import ch.bailu.gtk.builder.BuilderInterface
import ch.bailu.gtk.builder.ModelBuilder
import ch.bailu.gtk.log.Logable
import ch.bailu.gtk.parser.Parser
import ch.bailu.gtk.table.AliasTable
import ch.bailu.gtk.table.CallbackTable
import ch.bailu.gtk.table.StructureTable
import org.xmlpull.v1.XmlPullParserException
import java.io.*


fun main(args: Array<String>) {
    try {
        Configuration.init(args)
        println("1. fill tables")
        parse(AliasBuilder())
        println("2. log tables")
        logTables()
        println("3. build model and write code files")
        parse(ModelBuilder())
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@Throws(IOException::class)
private fun logTables() {
    logTable(StructureTable, Configuration.LOG_STRUCTURE_TABLE_FILE)
    logTable(AliasTable, Configuration.LOG_ALIAS_TABLE_FILE)
    logTable(CallbackTable, Configuration.LOG_CALLBACK_TABLE_FILE)
}

@Throws(IOException::class)
private fun logTable(logable: Logable, file: String) {
    println(file)
    var out: Writer? = null
    try {
        out = BufferedWriter(FileWriter(file))
        logable.log(out)
    } finally {
        out?.close()
    }
}


@Throws(IOException::class, XmlPullParserException::class)
fun parse(builder: BuilderInterface) {
    for (girFile in Configuration.GIR_FILES) {
        val file = getExistingFile(girFile)
        Parser(file, builder)
    }
}

@Throws(IOException::class)
private fun getExistingFile(girFile: String): File {
    var result = File(Configuration.GIR_DIR_CUSTOM, girFile)
    var type = "custom"
    if (!result.exists()) {
        result = File(Configuration.getInstance().girBaseDir, girFile)
        type = "system"
        if (!result.exists()) {
            result = File(Configuration.GIR_DIR_LOCAL, girFile)
            type = "local"
            if (!result.exists()) {
                throw IOException("File does not exist $result")
            }
        }
    }
    println("$type: $result")
    return result
}