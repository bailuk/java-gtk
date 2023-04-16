package ch.bailu.gtk

import ch.bailu.gtk.model.type.NamespaceType
import ch.bailu.gtk.table.AliasTable.add
import ch.bailu.gtk.table.AliasTable.convert
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TestAlias {
    @Test
    fun testAlias() {
        val from = NamespaceType("gtk", "Allocation")
        val to = NamespaceType("gdk", "Rectangle")
        add(from, to)
        val test = convert(from)
        Assertions.assertEquals("Rectangle", test.name)
        Assertions.assertEquals("gdk", test.namespace)
    }
}
