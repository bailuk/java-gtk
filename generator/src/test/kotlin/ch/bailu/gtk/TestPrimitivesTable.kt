package ch.bailu.gtk

import ch.bailu.gtk.table.PrimitivesTable.contains
import ch.bailu.gtk.table.PrimitivesTable.convert
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TestPrimitivesTable {
    @Test
    fun test() {
        Assertions.assertEquals(true, contains("gdouble"))
        Assertions.assertEquals("double", convert("gdouble"))
        Assertions.assertEquals(true, contains("double"))
        Assertions.assertEquals("double", convert("double"))
    }
}
