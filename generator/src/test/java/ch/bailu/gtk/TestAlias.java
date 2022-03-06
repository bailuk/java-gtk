package ch.bailu.gtk;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.bailu.gtk.converter.NamespaceType;
import ch.bailu.gtk.table.AliasTable;

public class TestAlias {

    @Test void testAlias() {
        var from = new NamespaceType("gtk", "Allocation");
        var to = new NamespaceType("gdk", "Rectangle");


        AliasTable.INSTANCE.add(from, to);

        var test = AliasTable.INSTANCE.convert(from);

        assertEquals("Rectangle", test.getName());
        assertEquals("gdk", test.getNamespace());
    }
}
