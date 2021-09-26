package ch.bailu.gtk.wrapper;

import ch.bailu.gtk.Pointer;

public class Dbl extends Pointer {

    private boolean created;

    public Dbl() {
        this(createDbl(0d));
    }

    public Dbl(long pointer) {
        super(pointer);
        created = pointer != 0;
    }


    private static long createDbl(double value) {
        return ImpDbl.createDbl(value);
    }

    public static Dbl create(double value) {
        return new Dbl(createDbl(value));
    }

    public void set(double i) {
        ImpDbl.set(getCPointer(), i);
    }

    public double get() {
        return ImpDbl.get(getCPointer());
    }

    public void destroy() {
        if (created) {
            ImpUtil.destroy(getCPointer());
            created = false;
        }
    }
}
