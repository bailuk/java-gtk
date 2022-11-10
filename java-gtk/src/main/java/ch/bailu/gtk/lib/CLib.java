package ch.bailu.gtk.lib;

import com.sun.jna.Library;
import com.sun.jna.Native;


public class CLib {
    public static final Api INST = Native.load("c", Api.class);

    public static Api API() {
        return INST;
    }

    public interface Api extends Library {
        long malloc(long size);
        long memset(long pointer, int value, long size);
        void free(long pointer);
    }

    public static long allocate(long size) {
        long result = API().malloc(size);
        API().memset(result, 0, size);
        return result;
    }
}
