package ch.bailu.gtk;

import org.junit.jupiter.api.Test;

import ch.bailu.gtk.table.PrimitivesTable;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestPrimitivesTable {
    @Test
    void test() {
        assertEquals(true, PrimitivesTable.INSTANCE.contains("gdouble"));
        assertEquals("double", PrimitivesTable.INSTANCE.convert("gdouble"));


        assertEquals(true, PrimitivesTable.INSTANCE.contains("double"));
        assertEquals("double", PrimitivesTable.INSTANCE.convert("double"));

    }
}
