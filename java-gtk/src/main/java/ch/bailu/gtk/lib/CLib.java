package ch.bailu.gtk.lib;

import com.sun.jna.Library;
import com.sun.jna.Native;


public class CLib {

    private static Instance _INST = null;

    public static Instance INST() {
        if (_INST == null) {
            _INST = Native.load("msvcrt", Instance.class);
        }
        return _INST;
    }

    public interface Instance extends Library {
        long malloc(long size);
        long memset(long pointer, int value, long size);
        void free(long pointer);
    }

    public static long allocate(long size) {
        long result = INST().malloc(size);
        INST().memset(result, 0, size);
        return result;
    }
}
