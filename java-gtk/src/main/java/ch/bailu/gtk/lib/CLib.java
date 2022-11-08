package ch.bailu.gtk.lib;

import com.sun.jna.Library;
import com.sun.jna.Native;


public class CLib {
    private static Api _API = null;

    public static Api API() {
        if (_API == null) {
            _API = Native.load("c", Api.class);
        }
        return _API;
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
