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

        println("==> fill tables")
        parse(AliasBuilder())

        println("==> log tables")
        logTables()

        println("==> build model and write code files")
        parse(ModelBuilder())

    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@Throws(IOException::class, XmlPullParserException::class)
fun parse(builder: BuilderInterface) {
    Configuration.NAMESPACES.forEach {Parser(it, builder, !it.notAvailable())}
}

@Throws(IOException::class)
private fun logTables() {
    logTable(StructureTable, Configuration.LOG_STRUCTURE_TABLE_FILE)
    logTable(AliasTable, Configuration.LOG_ALIAS_TABLE_FILE)
    logTable(CallbackTable, Configuration.LOG_CALLBACK_TABLE_FILE)
}

@Throws(IOException::class)
private fun logTable(logable: Logable, file: String) {
    println("  --> ${file}")
    var out: Writer? = null
    try {
        out = BufferedWriter(FileWriter(file))
        logable.log(out)
    } finally {
        out?.close()
    }
}
