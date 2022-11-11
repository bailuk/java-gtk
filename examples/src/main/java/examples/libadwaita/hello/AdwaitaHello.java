package examples.libadwaita.hello;

import ch.bailu.gtk.adw.Adw;
import ch.bailu.gtk.adw.Application;
import ch.bailu.gtk.adw.PreferencesWindow;
import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gtk.ApplicationWindow;
import ch.bailu.gtk.gtk.Label;
import ch.bailu.gtk.type.Strs;


/**
 * https://gitlab.gnome.org/GNOME/libadwaita/-/blob/main/examples/hello-world/hello.c
 *
 */
public class AdwaitaHello {
    public static void main(String[] args) {
        new AdwaitaHello(args);
    }

    PreferencesWindow prefs;
    public AdwaitaHello(String[] args) {
        Adw.init();
        Application app = new Application("org.example.Hello", ApplicationFlags.FLAGS_NONE);
        app.onActivate(() -> {
            var window = new ApplicationWindow(app);
            var label = new Label("Hello, World");
            window.setDefaultSize(200, 200);
            window.setChild(label);
            window.present();
        });
        app.run(args.length, new Strs(args));
    }
}
