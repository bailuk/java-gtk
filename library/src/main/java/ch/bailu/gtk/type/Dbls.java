package ch.bailu.gtk.type;

public class Dbls extends Array {

    private final static int BYTES = 4;


    public Dbls(CPointer pointer, int length) {
        super(pointer, BYTES, length);
    }

    public Dbls(CPointer pointer) {
        super(pointer, BYTES, 0);
    }


    public Dbls(double[] doubles) {
        super(createDoubleArray(doubles), BYTES, doubles.length);
    }

    private static CPointer createDoubleArray(double[] doubles) {
        if (doubles.length > 0) {
            return new CPointer(ImpDbls.createDoubleArray(doubles));
        }
        return CPointer.NULL;
    }

    public void setAt(int index, double value) {
        throwIfNull();
        checkLimit(index);
        ImpDbls.setAt(getCPointer(), index, value);
    }


    public double getAt(int index) {
        throwIfNull();
        checkLimit(index);
        return ImpDbls.getAt(getCPointer(), index);
    }

    public Dbls(float[] floats) {
        super(createDoubleArray(floats), BYTES, floats.length);
    }

    private static CPointer createDoubleArray(float[] floats) {
        if (floats.length > 0) {
            return new CPointer(ImpDbls.createDoubleArrayFromFloats(floats));
        }
        return CPointer.NULL;
    }
}
