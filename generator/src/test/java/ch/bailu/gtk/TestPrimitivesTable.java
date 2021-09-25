package ch.bailu.gtk;

import org.junit.jupiter.api.Test;

import ch.bailu.gtk.table.PrimitivesTable;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPrimitivesTable {
    @Test
    void test() {
        assertEquals(true, PrimitivesTable.instance().contains("gdouble"));
        assertEquals("double", PrimitivesTable.instance().convert("gdouble"));


        assertEquals(true, PrimitivesTable.instance().contains("double"));
        assertEquals("double", PrimitivesTable.instance().convert("double"));

    }
}
