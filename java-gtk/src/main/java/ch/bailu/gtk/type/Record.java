package ch.bailu.gtk.type;

import ch.bailu.gtk.glib.Glib;

/**
 * Record: a structure that is not based on gobject
 *
 */
public class Record extends Pointer {
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
