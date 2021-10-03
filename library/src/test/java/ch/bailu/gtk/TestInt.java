package ch.bailu.gtk;

import org.junit.jupiter.api.Test;

import ch.bailu.gtk.type.Int;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestInt {

        static {
            System.loadLibrary("glue");
        }

        @Test
        public void testInt() {
            Int i = new Int();
            assertEquals(0, i.get());

            i.set(290);
            assertEquals(290, i.get());

            i.set(Integer.MAX_VALUE);
            assertEquals(Integer.MAX_VALUE, i.get());

            i.set(Integer.MIN_VALUE);
            assertEquals(Integer.MIN_VALUE, i.get());
        }

}
