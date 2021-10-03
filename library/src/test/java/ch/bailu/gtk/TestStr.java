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
        assertEquals(TEXT.getBytes().length, s.getSize());
        s.destroy();
        assertEquals(0, s.getSize());
    }

}
