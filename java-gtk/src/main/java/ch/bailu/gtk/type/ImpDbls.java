package ch.bailu.gtk.type;

import ch.bailu.gtk.lib.jna.CLib;

public class ImpDbls {

    public static long createDoubleArray(double[] doubles) {
        long result = CLib.INST().malloc((long) doubles.length * Double.BYTES);
        Pointer.toJnaPointer(result).write(0, doubles, 0, doubles.length);
        return result;
    }

    public static long createDoubleArrayFromFloats(float[] floats) {
        long result = CLib.INST().malloc((long) floats.length * Double.BYTES);
        com.sun.jna.Pointer p = Pointer.toJnaPointer(result);
        for (int i = 0; i< floats.length; i++) {
            p.setDouble((long) i *Double.BYTES, floats[i]);
        }
        return result;

    }

    public static void setAt(long cPointer, int index, double value) {
        Pointer.toJnaPointer(cPointer).setDouble((long) index * Double.BYTES, value);
    }

    public static long createDbl(double value) {
        long result = CLib.INST().malloc(Double.BYTES);
        Pointer.toJnaPointer(result).setDouble(0, value);
        return result;
    }

    public static double getAt(long cPointer, int index) {
        return Pointer.toJnaPointer(cPointer).getDouble((long) index * Double.BYTES);
    }
}
