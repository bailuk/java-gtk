package ch.bailu.gtk.type;

import ch.bailu.gtk.lib.jna.CLib;

class ImpDbl {

    public static long createDoubleArray(double[] doubles) {
        long result = CLib.INST().malloc((long) doubles.length * Double.BYTES);
        Pointer.asJnaPointer(result).write(0, doubles, 0, doubles.length);
        return result;
    }

    public static long createDoubleArrayFromFloats(float[] floats) {
        long result = CLib.INST().malloc((long) floats.length * Double.BYTES);
        com.sun.jna.Pointer p = Pointer.asJnaPointer(result);
        for (int i = 0; i< floats.length; i++) {
            p.setDouble((long) i *Double.BYTES, floats[i]);
        }
        return result;

    }

    public static void setAt(long cPointer, int index, double value) {
        Pointer.asJnaPointer(cPointer).setDouble((long) index * Double.BYTES, value);
    }

    public static long createDbl(double value) {
        long result = CLib.INST().malloc(Double.BYTES);
        Pointer.asJnaPointer(result).setDouble(0, value);
        return result;
    }

    public static long createFlt(float value) {
        long result = CLib.INST().malloc(Float.BYTES);
        Pointer.asJnaPointer(result).setFloat(0, value);
        return result;
    }

    public static double getAt(long cPointer, int index) {
        return Pointer.asJnaPointer(cPointer).getDouble((long) index * Double.BYTES);
    }

    public static long createFloatArrayFromFloats(float[] floats) {
        long result = CLib.INST().malloc((long) floats.length * Float.BYTES);
        com.sun.jna.Pointer p = Pointer.asJnaPointer(result);
        for (int i = 0; i< floats.length; i++) {
            p.setDouble((long) i * Float.BYTES, floats[i]);
        }
        return result;
    }
}
