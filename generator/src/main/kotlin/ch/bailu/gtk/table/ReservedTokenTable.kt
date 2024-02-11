package ch.bailu.gtk.table

object ReservedTokenTable {
    private val table: MutableMap<String, String> = HashMap()

    init {
        add("package", "_package")
        add("native", "_native")
        add("continue", "_continue")
        add("double", "_double")
        add("int", "_int")
        add("new", "_new")
        add("default", "_default")
        add("private", "_private")
        add("...", "_ellipsis")
        add("interface", "_interface")
        add("false", "_false")
        add("true", "_true")
        add("toString", "toStr")
        add("wait", "doWait")
    }

    private fun add(reserved: String, replacement: String) {
        table[reserved] = replacement
    }

    fun convert(token: String): String {
        return table[token] ?: convertNumber(token)
    }

    private fun convertNumber(token: String): String {
        if (token.isNotEmpty() && token[0].isDigit()) {
            return "_$token"
        }
        return token
    }
}
