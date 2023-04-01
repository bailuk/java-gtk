package ch.bailu.gtk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.bailu.gtk.gdk.RGBA;
import ch.bailu.gtk.gio.Application;
import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.glib.Glib;
import ch.bailu.gtk.glib.MainContext;
import ch.bailu.gtk.glib.MainLoop;
import ch.bailu.gtk.type.PointerContainer;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;

public class TestCCall {

    @Test
    public void testRGBAMallocAndFileds() {
        RGBA rgba = new RGBA();
        assertEquals(0f, rgba.getFieldAlpha(),0f);
        assertEquals(0f, rgba.getFieldRed(),0f);
        assertEquals(0f, rgba.getFieldGreen(),0f);
        assertEquals(0f, rgba.getFieldBlue(),0f);

        rgba.setFieldAlpha(0.1f);
        rgba.setFieldRed(0.2f);
        rgba.setFieldGreen(0.3f);
        rgba.setFieldBlue(0.4f);
        assertEquals(0.1f, rgba.getFieldAlpha(),0f);
        assertEquals(0.2f, rgba.getFieldRed(),0f);
        assertEquals(0.3f, rgba.getFieldGreen(),0f);
        assertEquals(0.4f, rgba.getFieldBlue(),0f);
    }

    private int i = 0;
    @Test
    public void testMainLoop()  {
        //https://docs.huihoo.com/symbian/nokia-symbian3-developers-library-v0.8/GUID-7FD05006-09C1-4EF4-A2EB-AD98C2FA8866.html
        i = 0;

        final long timeExpectMin = 100*10;
        final long start = System.currentTimeMillis();
        final var loop = new MainLoop(new MainContext(PointerContainer.NULL), true);
        Glib.timeoutAdd(100, (self, user_data) -> {
            System.out.println(i);
            i++;
            if (i==10) {
                loop.quit();
                self.unregister();
                return false;
            }
            return true;
        }, null);
        assertEquals(0, i);
        loop.run();
        assertEquals(10, i);
        assertTrue(System.currentTimeMillis() - start >= timeExpectMin);
        loop.unref();

    }

    @Test
    public void testApplicationLoop() {
        final var app = new Application(new Str("com.example.test"), ApplicationFlags.FLAGS_NONE);
        app.onActivate(app::quit);
        app.run(2, new Strs(new String[]{"test", "test"}));
    }
}
