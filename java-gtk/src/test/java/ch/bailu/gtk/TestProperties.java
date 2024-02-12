package ch.bailu.gtk;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.bailu.gtk.gobject.Object;
import ch.bailu.gtk.gobject.Value;
import ch.bailu.gtk.gtk.Button;
import ch.bailu.gtk.gtk.Gtk;
import ch.bailu.gtk.type.Record;
import ch.bailu.gtk.type.Str;

public class TestProperties {

    @Test
    public void testProperties() {
        Gtk.init();
        var button = new Button();
        var label = new Str("test");

        var value = new Value(new Record(100).cast());
        value.setObject(label);
        value.setGtype(Object.getTypeID());
        button.setProperty("label", value);
        assertEquals("test", button.getLabel());

    }
}
