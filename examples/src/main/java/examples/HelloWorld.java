package examples;

import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.ApplicationWindow;
import ch.bailu.gtk.gtk.Button;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;

public class HelloWorld {
    public static void main(String[] args) {
        var app = new Application(new Str("com.example.GtkApplication"),
                ApplicationFlags.FLAGS_NONE);

        app.onActivate(() -> {
            // Create a new window
            var window = new ApplicationWindow(app);

            // Create a new button
            var button = new Button();

            // Set button label
            button.setLabel(new Str("Hello, World!"));

            // When the button is clicked, close the window
            button.onClicked(window::close);

            window.setChild(button);
            window.show();
        });

        // Start application main loop
        var result = app.run(args.length, new Strs(args));

        // Terminate with exit code
        System.exit(result);
    }
}
