package ch.bailu.gtk;

import org.junit.jupiter.api.Test;

import ch.bailu.gtk.cairo.Cairo;
import ch.bailu.gtk.cairo.Format;
import ch.bailu.gtk.cairo.Surface;

import static org.junit.jupiter.api.Assertions.*;

public class TestSurface {
    static {
        System.loadLibrary("glue");
    }

    @Test
    public void testSurface() {
        Surface surface = Cairo.imageSurfaceCreate(Format.A8, 256,256);
        assertNotEquals(0, surface.toLong());
        surface.destroy();

        byte[] bytes = new byte[256*256];
        surface = Cairo.imageSurfaceCreateForData(bytes, Format.A8, 256, 256, 256);
        assertNotEquals(0, surface.toLong());
    }

}
