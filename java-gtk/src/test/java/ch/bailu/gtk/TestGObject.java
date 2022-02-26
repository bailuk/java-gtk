package ch.bailu.gtk;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import ch.bailu.gtk.gobject.Gobject;
import ch.bailu.gtk.type.Str;

public class TestGObject {
    static {
        try {
            GTK.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test void testGObject() {
        assertEquals(GTK.TRUE,  Gobject.signalIsValidName(new Str("activate")));
        assertEquals(GTK.FALSE, Gobject.signalIsValidName(new Str("äctivate")));
    }
}
