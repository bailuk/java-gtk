package ch.bailu.gtk.type;

public class Dbl extends Array {

    public Dbl(double d) {
        this(createDbl(d), 1);
    }
    public Dbl() {
        this(createDbl(0),1);
    }
    public Dbl(PointerContainer pointer, int length) {
        super(pointer, Double.BYTES, length);
    }
    public Dbl(PointerContainer pointer) {
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

    private static PointerContainer createDbl(double value) {
        return new PointerContainer(ImpDbl.createDbl(value));
    }

    private static PointerContainer createDoubleArray(double[] doubles) {
        if (doubles.length > 0) {
            return new PointerContainer(ImpDbl.createDoubleArray(doubles));
        }
        return PointerContainer.NULL;
    }

    public void setAt(int index, double value) {
        throwIfNull();
        checkLimit(index);
        ImpDbl.setAt(asCPointer(), index, value);
    }


    public double getAt(int index) {
        throwIfNull();
        checkLimit(index);
        return ImpDbl.getAt(asCPointer(), index);
    }

    private static PointerContainer createDoubleArray(float[] floats) {
        if (floats.length > 0) {
            return new PointerContainer(ImpDbl.createDoubleArrayFromFloats(floats));
        }
        return PointerContainer.NULL;
    }

    public void set(double i) {
        setAt(0,i);
    }
    public double get() {
        return getAt(0);
    }
}
