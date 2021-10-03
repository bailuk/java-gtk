package ch.bailu.gtk.type;

public class Dbl extends Dbls {

    public Dbl(double d) {
        super(createDbl(d), 1);
    }

    public Dbl() {
        super(createDbl(0),1);
    }

    public Dbl(long pointer) {
        super(pointer);
    }

    private static long createDbl(double value) {
        return ImpDbls.createDbl(value);
    }

    public static Dbl create(double value) {
        return new Dbl(createDbl(value));
    }

    public void set(double i) {
        setAt(0,i);
    }

    public double get() {
        return getAt(0);
    }
}
