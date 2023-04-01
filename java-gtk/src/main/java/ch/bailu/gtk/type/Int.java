package ch.bailu.gtk.type;

public class Int extends Wrapper {

    public final static Int NULL = new Int(new PointerContainer(0));

    private boolean created;

    public Int() {
        this(createInt(0));
    }

    public Int(PointerContainer pointer) {
        super(pointer);
        created = pointer.isNotNull();
    }


    private static PointerContainer createInt(int value) {
        return new PointerContainer(ImpInt.createInt(value));
    }

    public static Int create(int value) {
        return new Int(createInt(value));
    }

    public void set(boolean b) {
        set( (b) ? 1 : 0);
    }

    public void set(int i) {
        ImpInt.set(asCPointer(), i);
    }

    public int get() {
        return ImpInt.get(asCPointer());
    }

    public boolean is() {
        return get() != 0;
    }

    @Override
    public void destroy() {
        if (created) {
            ImpUtil.destroy(asCPointer());
            created = false;
        }
    }
}
