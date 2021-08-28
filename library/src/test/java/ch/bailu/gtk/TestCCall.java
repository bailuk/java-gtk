package ch.bailu.gtk;

import org.junit.jupiter.api.Test;

import ch.bailu.gtk.gdk.RGBA;
import ch.bailu.gtk.gobject.ValueArray;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gio.ApplicationFlags;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestCCall {


    static {
        System.loadLibrary("glue");
    }

    @Test
    public void testRGBAMallocAndFileds() {
        RGBA rgba = new RGBA();
        assertEquals(0d, rgba.getFieldAlpha(),0d);
        assertEquals(0d, rgba.getFieldRed(),0d);
        assertEquals(0d, rgba.getFieldGreen(),0d);
        assertEquals(0d, rgba.getFieldBlue(),0d);

        rgba.setFieldAlpha(0.1);
        rgba.setFieldRed(0.2);
        rgba.setFieldGreen(0.3);
        rgba.setFieldBlue(0.4);
        assertEquals(0.1d, rgba.getFieldAlpha(),0d);
        assertEquals(0.2d, rgba.getFieldRed(),0d);
        assertEquals(0.3d, rgba.getFieldGreen(),0d);
        assertEquals(0.4d, rgba.getFieldBlue(),0d);
    }
}
