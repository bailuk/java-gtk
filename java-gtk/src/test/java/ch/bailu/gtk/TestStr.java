package ch.bailu.gtk;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;

public class TestStr {


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

    @Test
    public void testStrs() {
        final String[] strings = {"test", "test2", null};

        final Strs strs = new Strs(strings);

        assertEquals(3,strs.getLength());
        assertEquals("test2", strs.get(1).toString());
        assertEquals(0, strs.get(2).getCPointer());
        assertEquals("", strs.get(2).toString());
    }

}
