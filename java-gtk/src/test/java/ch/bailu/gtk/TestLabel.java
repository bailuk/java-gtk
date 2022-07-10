package ch.bailu.gtk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.Label;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;

public class TestLabel {

    @Test
    public void testStr() {

        var app = new Application(new Str("com.example.GtkApplication"),
                ApplicationFlags.FLAGS_NONE);

        app.onActivate(() -> {
            Label label = new Label(new Str("test"));
            assertEquals("test", label.getLabel().toString());
        });

        app.run(0, Strs.NULL);
    }
}
