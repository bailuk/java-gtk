package ch.bailu.gtk;

import ch.bailu.gtk.gobject.Gobject;
import ch.bailu.gtk.type.Str;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestGObject {

    @Test void testGObject() {
        assertTrue(Gobject.signalIsValidName(new Str("activate")));
        assertFalse(Gobject.signalIsValidName(new Str("äctivate")));
        assertEquals((20) << (2), ch.bailu.gtk.gobject.Object.getTypeID());
    }
}
