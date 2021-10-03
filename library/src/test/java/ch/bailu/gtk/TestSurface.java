package ch.bailu.gtk;

import org.junit.jupiter.api.Test;

import ch.bailu.gtk.cairo.Cairo;
import ch.bailu.gtk.cairo.Context;
import ch.bailu.gtk.cairo.Format;
import ch.bailu.gtk.cairo.Surface;
import ch.bailu.gtk.type.Bytes;

import static org.junit.jupiter.api.Assertions.*;

public class TestSurface {
    static {
        System.loadLibrary("glue");
    }

    @Test
    public void testSurface() {
        Surface surface = Cairo.imageSurfaceCreate(Format.ARGB32, 256,256);
        assertNotEquals(0, surface.getCPointer());
        surface.destroy();

        byte[] bytesBuffer = new byte[256*256*4];
        for (int i = 0; i< 256*256*4; i++) {
            bytesBuffer[i]=0;
        }
        Bytes bytes = new Bytes(bytesBuffer);

        surface = Cairo.imageSurfaceCreateForData(bytes, Format.ARGB32, 256, 256, 256*4);
        assertNotEquals(0, surface.getCPointer());

        Context context = surface.createContext();
        assertNotEquals(0, context.getCPointer());

        assertEquals(bytesBuffer[0], bytes.getByte(0));

        context.setSourceRgb(1,1,1);
        context.paint();


        assertNotEquals(bytesBuffer[0], bytes.getByte(0));

        context.destroy();
        surface.destroy();
        bytes.destroy();

    }

}
