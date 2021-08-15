package examples;

import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.ApplicationWindow;
import ch.bailu.gtk.gtk.Box;
import ch.bailu.gtk.gtk.Button;
import ch.bailu.gtk.gtk.Dialog;
import ch.bailu.gtk.gtk.Entry;
import ch.bailu.gtk.gtk.Orientation;
import ch.bailu.gtk.gtk.VBox;
import ch.bailu.gtk.gtk.Widget;
import ch.bailu.gtk.gtk.Window;

public class Spinner {

    private ch.bailu.gtk.gtk.Spinner spinnerSensitive;
    private ch.bailu.gtk.gtk.Spinner spinner_unsensitive;




    public Spinner(String[] argv) {
        var app = new Application("com.example.GtkApplication",
                ApplicationFlags.FLAGS_NONE);

        app.onActivate(() -> {
            String helloString = "Hello world!";

            var window = new ApplicationWindow(app);

            window.setTitle(helloString);
            doSpinner(window);
            window.showAll();

        });

        app.run(argv.length, argv);
    }
    private Widget doSpinner(Window window) {
/*
        window = gtk_dialog_new_with_buttons ("Spinner",
                GTK_WINDOW (do_widget),
                0,
                _("_Close"),
                GTK_RESPONSE_NONE,
                NULL);
*/

        window.setResizable(0);

/*
        g_signal_connect (window, "response",
                G_CALLBACK (gtk_widget_destroy), NULL);
        g_signal_connect (window, "destroy",
                G_CALLBACK (gtk_widget_destroyed), &window);
*/

        Box vbox = new Box(Orientation.VERTICAL, 5);
        vbox.setBorderWidth(5);
        window.add(vbox);

        /* Sensitive */
        Box hbox = new Box(Orientation.HORIZONTAL, 5);
        spinnerSensitive = new ch.bailu.gtk.gtk.Spinner();
        hbox.add(spinnerSensitive);
        hbox.add(new Entry());
        vbox.add(hbox);


        /* Disabled */
        hbox = new Box(Orientation.HORIZONTAL, 5);
        spinner_unsensitive = new ch.bailu.gtk.gtk.Spinner();

        hbox.add(spinner_unsensitive);
        hbox.add(new Entry());
        vbox.add(hbox);
        hbox.setSensitive(0);
        Button button = Button.newWithLabelButton("Play");
        button.onClicked(()->{
            spinnerSensitive.start();
            spinner_unsensitive.start();
        });
        vbox.add(button);

        button = Button.newWithLabelButton("Stop");
        button.onClicked((()->{
            spinnerSensitive.stop();
            spinner_unsensitive.stop();
        }));

        vbox.add(button);

        /* Start by default to test for:
         * https://bugzilla.gnome.org/show_bug.cgi?id=598496 */
        spinnerSensitive.start();
        spinner_unsensitive.start();

        window.showAll();

        return window;
    }
}
