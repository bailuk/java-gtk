package ch.bailu.gtk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.bailu.gtk.gdk.Rectangle;

public class TestGdkRectangle {

    @Test public void testRectangle() {
        Rectangle r1 = new Rectangle();

        assertEquals(0, r1.getFieldX());
        assertEquals(0, r1.getFieldY());
        assertEquals(0, r1.getFieldWidth());
        assertEquals(0, r1.getFieldHeight());

        r1.setFieldY(5);
        r1.setFieldWidth(10);
        assertEquals(5, r1.getFieldY());
        assertEquals(10, r1.getFieldWidth());

        Rectangle r2 = new Rectangle();
        r2.setFieldY(5);
        r2.setFieldWidth(10);

        assertTrue(r1.equal(r2));

        r2.setFieldHeight(2);
        assertEquals(2, r2.getFieldHeight());
        assertFalse(r1.equal(r2));
    }
}
