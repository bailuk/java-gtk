package examples;

import ch.bailu.gtk.exception.AllocationError;
import ch.bailu.gtk.geoclue.AccuracyLevel;
import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.ApplicationWindow;
import ch.bailu.gtk.gtk.Button;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;
import ch.bailu.gtk.geoclue.Simple;

public class GeoclueSample {
    public GeoclueSample(String[] args) {
        var app = new Application(new Str("com.example.GtkApplication"),
                ApplicationFlags.FLAGS_NONE);

        app.onActivate(() -> {


            try {
                var simple = Simple.newSyncSimple(new Str("geoclue-where-am-i"), AccuracyLevel.EXACT, null);
                simple.getLocation();
            } catch (AllocationError e) {
                System.err.println(e.getMessage());
            }

            // Create a new window
            var window = new ApplicationWindow(app);

            // Create a new button
            var button = Button.newWithLabelButton(new Str("Hello, World!"));

            // When the button is clicked, close the window
            button.onClicked(window::close);
            window.setChild(button);
            window.show();
        });

        app.run(args.length, new Strs(args));
    }
}
