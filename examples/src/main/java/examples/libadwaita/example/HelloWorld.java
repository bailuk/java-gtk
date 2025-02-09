package examples.libadwaita.example;

import ch.bailu.gtk.adw.Application;
import ch.bailu.gtk.adw.Bin;
import ch.bailu.gtk.gtk.ApplicationWindow;
import ch.bailu.gtk.gtk.Label;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;

public class HelloWorld {
    public static void main(String[] args) {
        var app = new Application(new Str("org.example.Hello"), 0);

        app.onActivate(() -> {
            var bin = new Bin();
            var win = new ApplicationWindow(app);
            var label = new Label(new Str("Hello"));
            win.setTitle(new Str("Hello"));
            win.setDefaultSize(200, 200);
            win.setChild(bin);
            bin.setChild(label);
            win.present();
        });

        app.run(0, Strs.NULL);
    }
}
