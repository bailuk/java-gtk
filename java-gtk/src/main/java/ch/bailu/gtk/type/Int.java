package ch.bailu.gtk.type;

import ch.bailu.gtk.glib.Glib;

public class Int extends Wrapper {

    public final static Int NULL = new Int(new PointerContainer(0));

    private boolean created; // prevent double destroy

    public Int(int value) {
        this(createInt(value));
    }

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
            Glib.free(asPointer());
            created = false;
        }
    }

    public static int getTypeID() {
        return 24; // G_TYPE_INT
    }

    public static long getBooleanTypeID() {
        return 20; // G_TYPE_BOOLEAN
    }
}
