package examples.libadwaita.demo_official;

import ch.bailu.gtk.adw.Application;
import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.type.Strs;

/**
 * https://gitlab.gnome.org/GNOME/libadwaita/-/blob/main/demo/adwaita-demo.c
 *
 * - G_DEFINE_FINAL_TYPE(
 *      AdwDemoWindow,               // namespace -> Generated Type
 *      adw_demo_window,             // namespace -> member prefix
 *      ADW_TYPE_APPLICATION_WINDOW  // subtype
 *   )
 *
 * - struct _AdwDemoWindow
 *      - Data (reference to parent and members)
 *
 * - adw_demo_window_class_init      // called when class gets initialised
 * - adw_demo_window_init            // called when instance gets initialised
 *
 */
public class AdwaitaDemoOfficial {
    public static void main(String[] args) {

        var app = new Application("org.gnome.Adwaita1.Demo", ApplicationFlags.NON_UNIQUE);

        app.onActivate(() -> {
            var window = new AdwDemoWindow(app);
            window.present();
        });
        app.run(args.length, new Strs(args));
        app.unref();
    }
}
