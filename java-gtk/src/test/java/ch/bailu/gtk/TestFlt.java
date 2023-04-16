package ch.bailu.gtk;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.bailu.gtk.type.Flt;

public class TestFlt {
    @Test
    public void testFlt() {
        Flt flt = new Flt();
        assertEquals(0f, flt.get());

        flt.set(290.99f);
        assertEquals(290.99f, flt.get());

        flt.set(Float.MAX_VALUE);
        assertEquals(Float.MAX_VALUE, flt.get());

        flt.destroy();
    }

    @Test
    public void testFlts() {
        Flt flts = new Flt(new float[] {4f,Float.MAX_VALUE,7f,0f,-1f});

        assertEquals(5, flts.getLength());
        assertEquals(Float.MAX_VALUE, flts.getAt(1));
        assertEquals(-1f, flts.getAt(flts.getLength()-1));

        flts.destroy();
    }
}
