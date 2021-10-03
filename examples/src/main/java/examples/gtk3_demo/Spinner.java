package examples.gtk3_demo;

import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.ApplicationWindow;
import ch.bailu.gtk.gtk.Button;
import ch.bailu.gtk.gtk.Dialog;
import ch.bailu.gtk.gtk.Window;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;

public class Spinner {

    private ch.bailu.gtk.gtk.Spinner spinnerSensitive;
    private ch.bailu.gtk.gtk.Spinner spinner_unsensitive;




    public Spinner(String[] argv) {
        var app = new Application(new Str("com.example.GtkApplication"),
                ApplicationFlags.FLAGS_NONE);



        app.onActivate(() -> {
            var window = new ApplicationWindow(app);
            var button = new Button();
            button.setLabel(new Str("run"));
            button.onClicked(() -> doSpinner(window));
            window.add(button);
            window.showAll();
        });

        app.run(argv.length, new Strs(argv));
    }
    private void doSpinner(Window parent) {

        var dialog =  Dialog.newWithButtonsDialog(new Str("Spinner"), parent, 0, new Str("Close"));
/*

        dialog.setResizable(GTK.FALSE);
        dialog.onResponse(new Dialog.OnResponse() {
            @Override
            public void onResponse(int response_id) {
                dialog.close();
            }
        });

        dialog.onDestroy(new Widget.OnDestroy() {
            @Override
            public void onDestroy() {

            }
        });

        Box vbox = new Box(Orientation.VERTICAL, 5);
        vbox.setBorderWidth(5);
        dialog.add(vbox);

        */
/* Sensitive *//*

        Box hbox = new Box(Orientation.HORIZONTAL, 5);
        spinnerSensitive = new ch.bailu.gtk.gtk.Spinner();
        hbox.add(spinnerSensitive);
        hbox.add(new Entry());
        vbox.add(hbox);


        */
/* Disabled *//*

        hbox = new Box(Orientation.HORIZONTAL, 5);
        spinner_unsensitive = new ch.bailu.gtk.gtk.Spinner();

        hbox.add(spinner_unsensitive);
        hbox.add(new Entry());
        vbox.add(hbox);
        hbox.setSensitive(0);
        Button button = Button.newWithLabelButton(new Str("Play"));
        button.onClicked(()->{
            spinnerSensitive.start();
            spinner_unsensitive.start();
        });
        vbox.add(button);

        button = Button.newWithLabelButton(new Str("Stop"));
        button.onClicked((()->{
            spinnerSensitive.stop();
            spinner_unsensitive.stop();
        }));

        vbox.add(button);

        */
/* Start by default to test for:
         * https://bugzilla.gnome.org/show_bug.cgi?id=598496 *//*

        spinnerSensitive.start();
        spinner_unsensitive.start();

        dialog.showAll();

        return dialog;
*/
    }
}
