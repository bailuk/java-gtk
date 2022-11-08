package ch.bailu.gtk.lib.callback;

public class CallbackID {
    private static long next = 0;

    public static synchronized long gen() {
        return next++;
    }
}
