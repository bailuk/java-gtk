package ch.bailu.gtk.table

import java.util.HashMap

/**
 * https://simonv.fr/TypesConvert/?integers
 * See [ch.bailu.gtk.model.filter.filterValues] for filtered values
 */
object ValueTable {

    private val table: MutableMap<String, String> = HashMap(50)

    init {
        add("2147483649", "-2147483647")
        add("2147483650", "-2147483646")
        add("2147483651", "-2147483645")
        add("2147483652", "-2147483644")
        add("2147483653", "-2147483643")
        add("2147483654", "-2147483642")
        add("2147483655", "-2147483641")
        add("2147483656", "-2147483640")
        add("2147483648", "-2147483648") // G_MININT32
        add("4294967295", "-1")          // G_MAXUINT32
        add("18446744073709551615", "-1") // Long variant
    }

    private fun add(original: String, converted: String) {
        table[original] = converted
    }

    operator fun contains(original: String): Boolean {
        return table.containsKey(original)
    }

    fun convert(original: String): String {
        return table[original] ?: original
    }
}
