package ch.bailu.gtk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.bailu.gtk.gio.ListStore;
import ch.bailu.gtk.gtk.TextTag;

public class TestPropertyAccess {
    @Test
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
}
