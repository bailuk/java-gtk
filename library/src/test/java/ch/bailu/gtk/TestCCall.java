package ch.bailu.gtk;

import org.junit.jupiter.api.Test;

import ch.bailu.gtk.gobject.ValueArray;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gio.ApplicationFlags;

public class TestCCall {


    static {
        System.loadLibrary("glue");
    }

    @Test
    public void testCCall() {
        ValueArray valueArray = new ValueArray(4);
        //valueArray.getNth(0);
    }

    @Test
    public void testApplication() {
        new Application("com.example.GtkApplication",
                ApplicationFlags.FLAGS_NONE);

    }
}
