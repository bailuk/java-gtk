package ch.bailu.gtk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;

import ch.bailu.gtk.gdk.Display;
import ch.bailu.gtk.gio.ListStore;
import ch.bailu.gtk.glib.Glib;
import ch.bailu.gtk.gtk.Box;
import ch.bailu.gtk.gtk.Button;
import ch.bailu.gtk.gtk.Gtk;
import ch.bailu.gtk.gtk.Orientation;
import ch.bailu.gtk.gtk.TextTag;
import ch.bailu.gtk.type.Str;

public class TestPropertyAccess {
    public static boolean gtkInit() {
        if ("true".equals(System.getProperty("java-gtk.headless"))) {
            return false;
        } else {
            return Gtk.initCheck() && Display.getDefault().isNotNull(); // Does not work inside Windows container
        }
    }

    public static boolean checkVersion() {
        return  Glib.checkVersion(2, 44,0).isNull();
    }

    @Test
    @EnabledIf("checkVersion")
    public void testPropertyAccess() {

        var listStore = new ListStore(TextTag.getTypeID());
        var textTag = new TextTag("test");

        assertEquals(0, listStore.getIntProperty("n-items"));
        listStore.append(textTag);
        assertEquals(1, listStore.getIntProperty("n-items"));

        var textTagGet = new TextTag(listStore.asListModel().getItem(0).cast());
        assertEquals("test", textTagGet.getStringProperty("name"));
        assertEquals("test", textTag.getStringProperty("name"));

        textTag.setBooleanProperty("accumulative-margin", false);
        assertFalse(textTag.getBooleanProperty("accumulative-margin"));
        textTag.setBooleanProperty("accumulative-margin", true);
        assertTrue(textTag.getBooleanProperty("accumulative-margin"));
    }

    @Test
    @EnabledIf("gtkInit")
    public void testPropertyAccessButton() {
        var button = new Button();
        var label = new Str("test2");

        button.setStringProperty("label", "test");
        assertEquals("test", button.getLabel().toString());
        assertEquals("test", button.getStringProperty("label"));

        button.setStrProperty("label", label);
        assertEquals("test2", button.getLabel().toString());

        assertTrue(button.getChild().isNotNull());
        assertTrue(button.getObjectProperty("child").isNotNull());

        assertEquals(button.getChild(), button.getObjectProperty("child"));
    }

    @Test
    @EnabledIf("gtkInit")
    public void testPropertyAccessBox() {
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
