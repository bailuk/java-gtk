package ch.bailu.gtk;

import org.junit.jupiter.api.Test;

import ch.bailu.gtk.type.Dbl;
import ch.bailu.gtk.type.Dbls;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

public class TestDbl {
    static {
        try {
            GTK.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    @Test
    public void testDbls() {
        Dbls d = new Dbls(new double[] {4d,5d,6d,7d,0d,-1d});
        Dbls f = new Dbls(new float[] {4f,5f,7f,0f,-1f});

        assertEquals(6, d.getLength());
        assertEquals(5, f.getLength());

        assertEquals(5d, d.getAt(1));
        assertEquals(-1f, f.getAt(f.getLength()-1));

        d.destroy();
        f.destroy();
    }
}
