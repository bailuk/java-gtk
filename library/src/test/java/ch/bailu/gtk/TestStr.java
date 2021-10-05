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
        final String TEXT = "I ♥ GTK+";
        Str s = new Str(TEXT);

        assertEquals(TEXT, s.toString());

        // getBytes is not null terminated
        assertEquals(TEXT.getBytes().length+1, s.getSize());
        s.destroy();
        assertEquals(0, s.getSize());

        s = new Str("");
        assertEquals("", s.toString());
        assertEquals("".getBytes().length+1, s.getSize());
        assertEquals(0, s.getByte(0));
        assertEquals(1, s.getSize());
        s.destroy();
        assertEquals(0, s.getSize());

    }

}
