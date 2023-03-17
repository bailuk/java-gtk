package ch.bailu.gtk.type;

import ch.bailu.gtk.lib.jna.CLib;

public class ImpFlt {
    public static void setAt(long cPointer, int index, float value) {
        Pointer.toJnaPointer(cPointer).setFloat((long) index * Float.BYTES, value);
    }

    public static long createFlt(float value) {
        long result = CLib.INST().malloc(Float.BYTES);
        Pointer.toJnaPointer(result).setFloat(0, value);
        return result;
    }

    public static float getAt(long cPointer, int index) {
        return Pointer.toJnaPointer(cPointer).getFloat((long) index * Float.BYTES);
    }

    public static long createFloatArray(float[] floats) {
        long result = CLib.INST().malloc((long) floats.length * Float.BYTES);
        com.sun.jna.Pointer p = Pointer.toJnaPointer(result);
        for (int i = 0; i< floats.length; i++) {
            p.setFloat((long) i * Float.BYTES, floats[i]);
        }
        return result;
    }
}
