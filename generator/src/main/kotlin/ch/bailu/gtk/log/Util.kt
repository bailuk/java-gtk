package ch.bailu.gtk.log

fun colonList(strings: Array<String>): String {
    return colonList(strings.toList())
}

fun colonList(strings: List<String>): String {
    var del = ""
    val builder = StringBuilder()
    builder.append("[")
    for (s in strings) {
        builder.append(del)
        builder.append(s)
        del = ":"
    }
    return builder.append("]").toString()
}
