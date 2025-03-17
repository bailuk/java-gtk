package ch.bailu.gtk.type;

import ch.bailu.gtk.glib.Glib;

/**
 * Record: a structure that is not based on gobject
 *
 */
public class Record extends Pointer {

    /**
     * Creates an empty record of specified size.
     * Allocates heap of specified size and initializes memory to 0.
     * @param size size to allocate
     */
    public Record(int size) {
        super(Glib.malloc0(size).cast());
    }

    public Record(PointerContainer pointer) {
        super(pointer);
    }

    /**
     * Frees resources.
     * Free memory allocated for this record.
     * Object is invalid afterwards and should not be accessed.
     */
    public void destroy() {
        Glib.free(asPointer());
    }
}
