package ch.bailu.gtk;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;

public class TestStr {

    @Test
    public void testUTF8() {
        // assertEquals("UTF-8", u8(Native.getDefaultStringEncoding()), "Property `jna.encoding` is not set to `UTF-8`");
        assertEquals(StandardCharsets.UTF_8.name(), u8(System.getProperty("file.encoding")), "Property `file.encoding` is not set to `UTF-8`");
        assertEquals(StandardCharsets.UTF_8.name(), u8(Charset.defaultCharset().name()), "Java default charset is not set to `UTF-8`");
    }

    private String u8(String in) {
        if (in.equals("UTF8")) {
            System.err.println("WARNING: Incorrect naming: Use `UTF-8` instead of `UTF8`");
            return StandardCharsets.UTF_8.name();
        }
        return in;
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

    @Test
    public void testStrs() {
        final String[] strings = {"test", "test2", null};

        final Strs strs = new Strs(strings);

        assertEquals(3,strs.getLength());
        assertEquals("test2", strs.get(1).toString());
        assertEquals(0, strs.get(2).asCPointer());
        assertEquals("", strs.get(2).toString());
    }

    @Test
    public void testNullStr() {
        Str str = Str.NULL;
        assertEquals(0, str.getSize());
        assertEquals(0, str.getLength());
    }
}
