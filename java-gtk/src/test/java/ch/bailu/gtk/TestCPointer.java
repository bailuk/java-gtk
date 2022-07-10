package ch.bailu.gtk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.bailu.gtk.type.CPointer;

public class TestCPointer {

    @Test
    public void testCPointer() {
        assertTrue(CPointer.NULL.isNull());
        assertFalse(CPointer.NULL.isNotNull());
    }
}
