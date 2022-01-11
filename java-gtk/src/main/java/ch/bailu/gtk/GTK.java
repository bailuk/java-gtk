package ch.bailu.gtk;

import java.io.IOException;

import ch.bailu.gtk.glue.LibLoader;

public class GTK {
    public final static int TRUE = 1;
    public final static int FALSE = 0;

    public static void init() throws IOException {
        new LibLoader();
    }

    /**
     * convert GTK boolean to Java boolean
     * @param bool value to convert
     * @return
     */
    public static boolean IS(int bool) {
        return is(bool);
    }

    /**
     * convert Java boolean to GTK boolean.
     * @param bool value to convert
     * @return
     */
    public static int IS(boolean bool) {
        return is(bool);
    }

    /**
     * convert GTK boolean to Java boolean
     * @param bool value to convert
     * @return
     */
    public static boolean is(int bool) {
        return bool == TRUE;
    }

    /**
     * convert Java boolean to GTK boolean
     * @param bool value to convert
     * @return
     */
    public static int is(boolean bool) {
        if (bool) {
            return TRUE;
        }
        return FALSE;
    }

    public static int TOGGLE(int bool) {
        if (IS(bool)) return GTK.FALSE;
        return GTK.TRUE;
    }
}
