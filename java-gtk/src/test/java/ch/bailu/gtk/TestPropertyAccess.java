package ch.bailu.gtk;

import ch.bailu.gtk.gtk.Box;
import ch.bailu.gtk.gtk.Button;
import ch.bailu.gtk.gtk.Gtk;
import ch.bailu.gtk.gtk.Orientation;
import ch.bailu.gtk.type.Str;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestPropertyAccess {

    @Test
    public void testPropertyAccessButton() {
        Gtk.init();
        var button = new Button();
        var label = new Str("test2");


        button.setStringProperty( "label", "test");
        assertEquals("test", button.getLabel().toString());
        assertEquals("test", button.getStringProperty("label"));

        button.setStrProperty("label", label);
        assertEquals("test2", button.getLabel().toString());

        assertTrue(button.getChild().isNotNull());
        assertTrue(button.getObjectProperty("child").isNotNull());

        assertEquals(button.getChild(), button.getObjectProperty("child"));
    }

    @Test
    public void testPropertyAccessBox() {
        Gtk.init();
        var box = new Box(Orientation.HORIZONTAL, 5);

        int spacing = box.getIntProperty("spacing");
        assertEquals(5, spacing);

        box.setIntProperty("spacing", 10);
        assertEquals(10, box.getSpacing());

        box.setHomogeneous(false);
        assertEquals(box.getHomogeneous(), box.getBooleanProperty("homogeneous"));
        assertFalse(box.getHomogeneous());

        box.setBooleanProperty("homogeneous", true);
        assertEquals(box.getCanFocus(), box.getBooleanProperty("homogeneous"));
        assertTrue(box.getHomogeneous());
    }
}
