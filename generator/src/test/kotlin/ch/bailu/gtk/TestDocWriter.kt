package ch.bailu.gtk

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TestDocWriter {
    @Test
    fun testEvenPre() {
        val doc = "Sets the maximum allowed length of the contents of the widget. If\n" +
                "the current contents are longer than the given length, then they\n" +
                "will be truncated to fit.\n" +
                "\n" +
                "This is equivalent to getting @entry's #GtkEntryBuffer and\n" +
                "calling gtk_entry_buffer_set_max_length() on it.\n" +
                "]|"


        Assertions.assertEquals(false, isEvenPre(doc))
    }

    private fun isEvenPre(doc: String): Boolean {
        return if (count(doc, "|[") != count(doc, "]|")) {
            println("WARNING code tags are not even: \"${doc.replace('\n', ' ')}\"")
            false
        } else {
            true
        }
    }

    private fun count(doc: String, word: String): Int {
        var index = doc.indexOf(word)
        var count = 0

        while (index > -1) {
            count++
            index = doc.indexOf(word, index+1)
        }
        return count
    }
}
