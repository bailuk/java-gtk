package ch.bailu.gtk.type;

public class Int extends Wrapper {

    private boolean created;

    public Int() {
        this(createInt(0));
    }

    public Int(CPointer pointer) {
        super(pointer);
        created = pointer.isNotNull();
    }


    private static CPointer createInt(int value) {
        return new CPointer(ImpInt.createInt(value));
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
