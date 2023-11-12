package ch.bailu.gtk.type;

import ch.bailu.gtk.glib.Glib;

/**
 * https://www.w3schools.com/java/java_data_types.asp
 */
public class Int64 extends Wrapper {
    private boolean created;

    public Int64() {
        this(createLong(0));
    }

    public Int64(PointerContainer pointer) {
        super(pointer);
        created = pointer.isNotNull();
    }


    private static PointerContainer createLong(long value) {
        return new PointerContainer(ImpInt.createLong(value));
    }

    public static Int64 create(long value) {
        return new Int64(createLong(value));
    }

    public void set(long i) {
        ImpInt.setLong(asCPointer(), i);
    }

    public long get() {
        return ImpInt.getLong(asCPointer());
    }

    @Override
    public void destroy() {
        if (created) {
            Glib.free(asPointer());
            created = false;
        }
    }
}
