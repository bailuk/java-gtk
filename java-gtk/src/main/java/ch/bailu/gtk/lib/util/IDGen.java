package ch.bailu.gtk.lib.util;

public class IDGen {
    private long next = 0;
    public synchronized long gen() {
        return next++;
    }
}
