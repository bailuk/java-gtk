package ch.bailu.gtk;

import org.junit.jupiter.api.Test;

import ch.bailu.gtk.type.Str;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestStr {
    static {
        System.loadLibrary("glue");
    }

    @Test
    public void testStr() {
        Str s = new Str("test");

        assertEquals("test", s.toString());
        assertEquals("test".length()+1, s.getSize());
        s.destroy();
        assertEquals(0, s.getSize());
    }

}
