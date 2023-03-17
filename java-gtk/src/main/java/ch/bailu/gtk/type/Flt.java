package ch.bailu.gtk.type;

public class Flt extends Array {

    public Flt(float f) {
        this(createFlt(f), 1);
    }
    public Flt() {
        this(createFlt(0),1);
    }
    public Flt(CPointer pointer, int length) {
        super(pointer, Float.BYTES, length);
    }
    public Flt(CPointer pointer) {
        super(pointer, Float.BYTES, 0);
    }
    public Flt(float[] values) {
        super(createFloatArray(values), Float.BYTES, values.length);
    }


    public void setAt(int index, float value) {
        throwIfNull();
        checkLimit(index);
        ImpFlt.setAt(getCPointer(), index, value);
    }


    public float getAt(int index) {
        throwIfNull();
        checkLimit(index);
        return ImpFlt.getAt(getCPointer(), index);
    }

    private static CPointer createFloatArray(float[] values) {
        if (values.length > 0) {
            return new CPointer(ImpFlt.createFloatArray(values));
        }
        return CPointer.NULL;
    }

    private static CPointer createFlt(float value) {
        return new CPointer(ImpDbl.createFlt(value));
    }

    public static Flt create(float value) {
        return new Flt(createFlt(value));
    }

    public void set(float i) {
        setAt(0,i);
    }

    public float get() {
        return getAt(0);
    }
}
