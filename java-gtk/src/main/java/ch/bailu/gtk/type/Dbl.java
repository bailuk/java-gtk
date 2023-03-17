package ch.bailu.gtk.type;

public class Dbl extends Array {

    public Dbl(double d) {
        this(createDbl(d), 1);
    }
    public Dbl() {
        this(createDbl(0),1);
    }
    public Dbl(CPointer pointer, int length) {
        super(pointer, Double.BYTES, length);
    }
    public Dbl(CPointer pointer) {
        super(pointer, Double.BYTES, 0);
    }
    public Dbl(double[] doubles) {
        super(createDoubleArray(doubles), Double.BYTES, doubles.length);
    }
    public Dbl(float[] floats) {
        super(createDoubleArray(floats), Double.BYTES, floats.length);
    }

    public static Dbl create(double value) {
        return new Dbl(createDbl(value));
    }

    private static CPointer createDbl(double value) {
        return new CPointer(ImpDbl.createDbl(value));
    }

    private static CPointer createDoubleArray(double[] doubles) {
        if (doubles.length > 0) {
            return new CPointer(ImpDbl.createDoubleArray(doubles));
        }
        return CPointer.NULL;
    }

    public void setAt(int index, double value) {
        throwIfNull();
        checkLimit(index);
        ImpDbl.setAt(getCPointer(), index, value);
    }


    public double getAt(int index) {
        throwIfNull();
        checkLimit(index);
        return ImpDbl.getAt(getCPointer(), index);
    }

    private static CPointer createDoubleArray(float[] floats) {
        if (floats.length > 0) {
            return new CPointer(ImpDbl.createDoubleArrayFromFloats(floats));
        }
        return CPointer.NULL;
    }

    public void set(double i) {
        setAt(0,i);
    }
    public double get() {
        return getAt(0);
    }
}
