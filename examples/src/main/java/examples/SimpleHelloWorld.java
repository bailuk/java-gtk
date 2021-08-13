package examples;

import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.ApplicationWindow;
import ch.bailu.gtk.gtk.Button;

public class SimpleHelloWorld {
    public SimpleHelloWorld(String[] argv) {
        var app = new Application("com.example.GtkApplication",
                ApplicationFlags.FLAGS_NONE);

        app.onActivate(() -> {
            String helloString = "Hello world!";

            // Create a new window
            var window = new ApplicationWindow(app);

            window.setTitle(helloString);
            // Create a new button
            var button = Button.newWithLabelButton(helloString);

            // When the button is clicked, close the window
            button.onClicked(() -> window.close());
            window.add(button);
            window.showAll();
            window.present();

        });

        app.run(argv.length, argv);
    }

}
