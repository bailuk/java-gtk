package ch.bailu.gtk.type;

import ch.bailu.gtk.glib.Glib;

class ImpDbl {

    public static long createDoubleArray(double[] doubles) {
        var result = Glib.malloc((long) doubles.length * Double.BYTES);
        result.asJnaPointer().write(0, doubles, 0, doubles.length);
        return result.asCPointer();
    }

    public static long createDoubleArrayFromFloats(float[] floats) {
        var result = Glib.malloc((long) floats.length * Double.BYTES);
        com.sun.jna.Pointer p = result.asJnaPointer();
        for (int i = 0; i< floats.length; i++) {
            p.setDouble((long) i *Double.BYTES, floats[i]);
        }
        return result.asCPointer();

    }

    public static void setAt(long cPointer, int index, double value) {
        Pointer.asJnaPointer(cPointer).setDouble((long) index * Double.BYTES, value);
    }

    public static long createDbl(double value) {
        var result = Glib.malloc(Double.BYTES);
        result.asJnaPointer().setDouble(0, value);
        return result.asCPointer();
    }

    public static double getAt(long cPointer, int index) {
        return Pointer.asJnaPointer(cPointer).getDouble((long) index * Double.BYTES);
    }
}
