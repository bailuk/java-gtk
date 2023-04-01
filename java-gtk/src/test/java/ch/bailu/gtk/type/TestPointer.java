package ch.bailu.gtk.type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TestPointer {

    @Test
    void testPointer() {
        assertEquals(45L, new Pointer(Type.cast(45L)).asCPointer());
        assertEquals(999L, Type.cast(new Pointer(Type.cast(999L)).asJnaPointer()).asCPointer());

        var pointer1 = Pointer.NULL;
        var pointer2 = new Pointer(Type.cast(0));

        assertTrue(pointer1.equals(pointer1));
        assertTrue(pointer1.equals(null));
        assertTrue(pointer1.equals(pointer2));
        assertTrue(pointer1.equals(0L));
        assertTrue(pointer1.equals(0f));

        var pointer3 = new Pointer(Type.cast(123));
        var pointer4 = new Pointer(pointer3.cast());
        assertFalse(pointer3.equals(pointer1));
        assertTrue(pointer3.equals(pointer4));
        assertTrue(pointer3.equals(123L));
        assertFalse(pointer3.equals(124L));
        assertTrue(pointer3.equals(123f));
        assertTrue(pointer3.equals(Type.cast(123)));
    }
}
