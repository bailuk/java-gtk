package ch.bailu.gtk.type;

import ch.bailu.gtk.glib.Glib;

class ImpFlt {
    public static void setAt(long cPointer, int index, float value) {
        Pointer.asJnaPointer(cPointer).setFloat((long) index * Float.BYTES, value);
    }

    public static long createFlt(float value) {
        var result = Glib.malloc(Float.BYTES);
        result.asJnaPointer().setFloat(0, value);
        return result.asCPointer();
    }

    public static float getAt(long cPointer, int index) {
        return Pointer.asJnaPointer(cPointer).getFloat((long) index * Float.BYTES);
    }


    public static long createFloatArray(float[] floats) {
        var result = Glib.malloc((long) floats.length * Float.BYTES);
        com.sun.jna.Pointer p = result.asJnaPointer();
        for (int i = 0; i< floats.length; i++) {
            p.setFloat((long) i * Float.BYTES, floats[i]);
        }
        return result.asCPointer();
    }
}
