package ch.bailu.gtk;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.bailu.gtk.type.PointerContainer;

public class TestCPointer {

    @Test
    public void testCPointer() {
        assertTrue(PointerContainer.NULL.isNull());
        assertFalse(PointerContainer.NULL.isNotNull());
    }
}
