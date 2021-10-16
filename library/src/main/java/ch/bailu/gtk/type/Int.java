package ch.bailu.gtk.type;

public class Int extends Wrapper {

    private boolean created;

    public Int() {
        this(createInt(0));
    }

    public Int(long pointer) {
        super(pointer);
        created = pointer != 0;
    }


    private static long createInt(int value) {
        return ImpInt.createInt(value);
    }

    public static Int create(int value) {
        return new Int(createInt(value));
    }

    public void set(int i) {
        ImpInt.set(getCPointer(), i);
    }

    public int get() {
        return ImpInt.get(getCPointer());
    }

    public void destroy() {
        if (created) {
            ImpUtil.destroy(getCPointer());
            created = false;
        }
    }
}
