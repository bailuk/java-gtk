package ch.bailu.gtk;

import org.junit.jupiter.api.Test;

import ch.bailu.gtk.type.Int;
import ch.bailu.gtk.type.Int64;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestInt {

    @Test
    public void testInt() {
        var a = new Int();
        assertEquals(0, a.get());

        a.set(290);
        assertEquals(290, a.get());

        a.set(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, a.get());

        a.set(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, a.get());
        a.destroy();

        var b = Int.create(-1);
        assertEquals(-1, b.get());
        b.destroy();
    }

    @Test
    public void testInt64() {
        var a = new Int64();
        assertEquals(0L, a.get());

        a.set(290);
        assertEquals(290L, a.get());

        a.set(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, a.get());

        a.set(Long.MIN_VALUE);
        assertEquals(Long.MIN_VALUE, a.get());
        a.destroy();

        var b = Int64.create(-1L);
        assertEquals(-1L, b.get());
        b.destroy();
    }
}
