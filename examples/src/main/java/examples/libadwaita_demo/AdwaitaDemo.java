package examples.libadwaita_demo;

import ch.bailu.gtk.adw.Adw;
import ch.bailu.gtk.adw.Application;
import ch.bailu.gtk.adw.PreferencesWindow;
import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gtk.ApplicationWindow;
import ch.bailu.gtk.gtk.Label;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;


public class AdwaitaDemo {
    PreferencesWindow prefs;
    public AdwaitaDemo(String[] args) {
        Adw.init();
        Application app = new Application(new Str("org.adw.example"), ApplicationFlags.FLAGS_NONE);
        app.onActivate(() -> {
            var window = new ApplicationWindow(app);
            var label = new Label(new Str("Hello, World"));
            window.setDefaultSize(200, 200);
            window.setChild(label);
            window.present();
        });
        app.run(args.length, new Strs(args));
    }
}
