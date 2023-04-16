package ch.bailu.gtk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import ch.bailu.gtk.cairo.Cairo;
import ch.bailu.gtk.cairo.Context;
import ch.bailu.gtk.cairo.Format;
import ch.bailu.gtk.cairo.Surface;
import ch.bailu.gtk.type.Bytes;

public class TestSurface {


    @Test
    public void testSurface() {
        Surface surface = Cairo.imageSurfaceCreate(Format.ARGB32, 256,256);
        assertNotEquals(0, surface.asCPointer());
        surface.destroy();

        byte[] bytesBuffer = new byte[256*256*4];
        for (int i = 0; i< 256*256*4; i++) {
            bytesBuffer[i]=0;
        }
        Bytes bytes = new Bytes(bytesBuffer);

        surface = Cairo.imageSurfaceCreateForData(bytes, Format.ARGB32, 256, 256, 256*4);
        assertNotEquals(0, surface.asCPointer());

        Context context = surface.createContext();
        assertNotEquals(0, context.asCPointer());

        assertEquals(bytesBuffer[0], bytes.getByte(0));

        context.setSourceRgb(1,1,1);
        context.paint();


        assertNotEquals(bytesBuffer[0], bytes.getByte(0));

        context.destroy();
        surface.destroy();
        bytes.destroy();

    }

    @Test
    public void testImageSurface() {
        int width = 23;
        int height = 32;

        Surface surface = Cairo.imageSurfaceCreate(Format.ARGB32, width,height);

        assertEquals(Format.ARGB32, surface.getFormat());
        assertEquals(width, surface.getWidth());
        assertEquals(height, surface.getHeight());
        assertEquals(width*4, surface.getStride());
        assertEquals(4, Cairo.formatStrideForWidth(Format.ARGB32, 1));

        Context context = surface.createContext();
        context.setSourceRgba(0.501,0.251,0.751,1);
        context.paint();

        Bytes data = surface.getData();

        int size = width*height;
        int x = 0;
        for (int i = 0; i< size; i++) {
            assertEquals(192,  toUnsigned(data.getByte(x++)));  // Blue
            assertEquals(64,   toUnsigned(data.getByte(x++)));  // Green
            assertEquals(128,  toUnsigned(data.getByte(x++)));  // Red
            assertEquals(255,  toUnsigned(data.getByte(x++)));  // Alpha
        }


        context.destroy();
        surface.destroy();

    }

    public static int toUnsigned(byte x) {
        return ((int) x) & 0xff;
    }

}
