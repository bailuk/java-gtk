package ch.bailu.gtk.wrapper;

public class ImpDbls {

    public static native long createDoubleArray(double[] doubles);
    public static native long createDoubleArrayFromFloats(float[] floats);

    public static native void setAt(long cPointer, int index, double value);

    public static native long createDbl(double value);

    public static native double getAt(long cPointer, int index);
}
