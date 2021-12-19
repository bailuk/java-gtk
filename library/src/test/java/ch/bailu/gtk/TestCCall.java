package ch.bailu.gtk;

import org.junit.jupiter.api.Test;

import ch.bailu.gtk.gdk.RGBA;
import ch.bailu.gtk.glib.Glib;
import ch.bailu.gtk.glib.MainContext;
import ch.bailu.gtk.glib.MainLoop;
import ch.bailu.gtk.type.CPointer;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

public class TestCCall {
    static {
        try {
            GTK.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private int i = 0;
    @Test
    public void testMainLoop()  {
        //https://docs.huihoo.com/symbian/nokia-symbian3-developers-library-v0.8/GUID-7FD05006-09C1-4EF4-A2EB-AD98C2FA8866.html
        i = 0;

        final long timeExpectMin = 100*10;
        final long start = System.currentTimeMillis();
        final var loop = new MainLoop(new MainContext(CPointer.NULL), GTK.TRUE);
        Glib.timeoutAdd(100, user_data -> {
            i++;
            if (i==10) {
                loop.quit();
                return GTK.FALSE;
            }
            return GTK.TRUE;
        }, null);
        assertEquals(0, i);
        loop.run();
        assertEquals(10, i);
        assertEquals(true, System.currentTimeMillis() - start >= timeExpectMin);
        loop.unref();

    }
/*
    @Test
    public void testApplicationLoop() {
        final var app = new Application(new Str("com.example.test"), ApplicationFlags.FLAGS_NONE);
        app.onActivate(() -> app.quit());
        app.run(2, new Strs(new String[]{"test", "test"}));
    }*/
}
