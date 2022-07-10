package ch.bailu.gtk;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
            Str text = new Str("test");
            Label label = new Label(text);
            text.destroy();
            assertEquals("test", label.getLabel().toString());

            for (int i = 0; i < 1000; i++) {
                String string = "this is a test string " + i;
                setText(label, string);
                assertEquals(string, label.getLabel().toString());
            }
        });

        app.run(0, Strs.NULL);
    }

    private static void setText(Label label, String text) {
        Str str = new Str(text);
        label.setText(str);
        str.destroy();
    }
}
