package ch.bailu.gtk;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.bailu.gtk.type.Dbl;

public class TestDbl {


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

    @Test
    public void testDbls() {
        Dbl d = new Dbl(new double[] {4d,Double.MAX_VALUE,6d,7d,0d,-1d});
        Dbl f = new Dbl(new float[] {4f,Float.MAX_VALUE,7f,0f,-1f});

        assertEquals(6, d.getLength());
        assertEquals(5, f.getLength());

        assertEquals(Double.MAX_VALUE, d.getAt(1));
        assertEquals(Float.MAX_VALUE, f.getAt(1));
        assertEquals(-1f, f.getAt(f.getLength()-1));

        d.destroy();
        f.destroy();
    }
}
