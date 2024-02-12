package ch.bailu.gtk;

import ch.bailu.gtk.gtk.Box;
import ch.bailu.gtk.gtk.Button;
import ch.bailu.gtk.gtk.Gtk;
import ch.bailu.gtk.gtk.Orientation;
import ch.bailu.gtk.lib.util.PropertyAccess;
import ch.bailu.gtk.type.Str;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestPropertyAccess {

    @Test
    public void testPropertyAccessButton() {
        Gtk.init();
        var button = new Button();
        var label = new Str("test2");

        PropertyAccess.setStringProperty(button, "label", "test");
        assertEquals("test", button.getLabel().toString());
        assertEquals("test", PropertyAccess.getStringProperty(button, "label"));

        button.setProperty("label", PropertyAccess.toValue(label));
        assertEquals("test2", button.getLabel().toString());

        assertTrue(button.getChild().isNotNull());
        assertTrue(PropertyAccess.getObjectProperty(button, "child").isNotNull());

        assertEquals(button.getChild(), PropertyAccess.getObjectProperty(button, "child"));
    }

    @Test
    public void testPropertyAccessBox() {
        Gtk.init();
        var box = new Box(Orientation.HORIZONTAL, 5);

        int spacing = PropertyAccess.getIntProperty(box, "spacing");
        assertEquals(5, spacing);

        PropertyAccess.setIntProperty(box, "spacing", 10);
        assertEquals(10, box.getSpacing());

        box.setHomogeneous(false);
        assertEquals(box.getHomogeneous(), PropertyAccess.getBooleanProperty(box, "homogeneous"));
        assertFalse(box.getHomogeneous());

        PropertyAccess.setBooleanProperty(box, "homogeneous", true);
        assertEquals(box.getCanFocus(), PropertyAccess.getBooleanProperty(box, "homogeneous"));
        assertTrue(box.getHomogeneous());
    }
}
