package ch.bailu.gtk.lib.bridge;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import ch.bailu.gtk.gdkpixbuf.Pixbuf;
import ch.bailu.gtk.gobject.Object;
import ch.bailu.gtk.lib.util.JavaResource;
import ch.bailu.gtk.type.Bytes;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.exception.AllocationError;

public class InputStreamBridgeTest {

    @Test
    public void testInputStreamBridge() throws AllocationError, IOException {
        System.out.println("Class Size: " + Object.getTypeSize().classSize);


        try (java.io.InputStream inputStream = new JavaResource("/test.txt").asStream()) {
            var inputStreamBridge = new InputStreamBridge(inputStream);

            var buffer = new Bytes(10);

            var count = (int)inputStreamBridge.read(buffer,buffer.getLength(), null);
            buffer.setByte(count, (byte)0);

            assertEquals(5, count);
            assertEquals("test\n", new Str(buffer.cast()).toString());
            inputStreamBridge.unref();
        }
    }

    @Test
    public void testReadImage() throws IOException, AllocationError {
        final var inputStreamBridge = new InputStreamBridge(new JavaResource("/GTK.svg").asStream());
        final var pixbuf = Pixbuf.newFromStreamAtScalePixbuf(inputStreamBridge, -1, -1, true, null);
        assertEquals(89, pixbuf.getWidth());
        assertEquals(96, pixbuf.getHeight());
        assertTrue(inputStreamBridge.close(null));
        inputStreamBridge.unref();
    }
}
