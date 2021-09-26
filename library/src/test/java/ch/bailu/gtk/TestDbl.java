package ch.bailu.gtk;

import org.junit.jupiter.api.Test;

import ch.bailu.gtk.wrapper.Dbl;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestDbl {

    static {
        System.loadLibrary("glue");
    }

    @Test
    public void testDbl() {
        Dbl d = new Dbl();
        assertEquals(0d, d.get());

        d.set(290.99);
        assertEquals(290.99, d.get());

        d.set(Double.MAX_VALUE);
        assertEquals(Double.MAX_VALUE, d.get());

        d.set(Double.MIN_VALUE);
        assertEquals(Double.MIN_VALUE, d.get());
    }
}
