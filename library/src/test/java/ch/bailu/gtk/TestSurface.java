package ch.bailu.gtk;

import org.junit.jupiter.api.Test;

import ch.bailu.gtk.cairo.Cairo;
import ch.bailu.gtk.cairo.Context;
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
        assertNotEquals(0, surface.getCPointer());
        surface.destroy();

        byte[] bytes = new byte[256*256*4];
        for (int i = 0; i< 256*256*4; i++) {
            bytes[i]=0;
        }

        surface = Cairo.imageSurfaceCreateForData(bytes, Format.ARGB32, 256, 256, 256*4);
        assertNotEquals(0, surface.getCPointer());

        Context context = surface.createContext();
        assertNotEquals(0, context.getCPointer());
        context.setSourceRgb(1,1,1);
        context.paint();

        for (int i = 0; i< 256*256*4; i++) {
            assertEquals(0, bytes[i]);
        }


        context.destroy();
        surface.destroy();

    }

}
