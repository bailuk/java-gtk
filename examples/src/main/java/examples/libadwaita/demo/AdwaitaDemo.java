package examples.libadwaita.demo;

import ch.bailu.gtk.adw.AboutWindow;
import ch.bailu.gtk.adw.Application;
import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gtk.License;
import ch.bailu.gtk.gtk.Window;
import ch.bailu.gtk.lib.bridge.GResource;
import ch.bailu.gtk.lib.handler.action.ActionHandler;
import ch.bailu.gtk.type.Strs;

/**
 * Almost complete port of the official demo from C to Java
 * https://gitlab.gnome.org/GNOME/libadwaita/-/blob/main/demo
 *
 */
public class AdwaitaDemo {
    private static final Strs developers = new Strs(new String[] {
                "Adrien Plazas",
                "Alexander Mikhaylenko",
                "Andrei Lișiță",
                "Guido Günther",
                "Jamie Murphy",
                "Julian Sparber",
                "Lukas Bai",
                "Manuel Genovés",
                "Zander Brown",
                null
    });

    private static final Strs designers = new Strs(new String[] {
            "GNOME Design Team",
            null
    });

    public static void main(String[] args) {
        GResource.loadAndRegister("/adw_demo/adwaita-demo.gresources.gresource");

        final var app = new Application("org.gnome.Adwaita1.Demo", ApplicationFlags.NON_UNIQUE);

        ActionHandler.get(app, "about").onActivate(() -> showAbout(app));
        ActionHandler.get(app, "preferences").onActivate(() -> showPreferences(app));
        ActionHandler.get(app, "inspector").onActivate(AdwaitaDemo::showInspector);

        app.onActivate(() -> {
            var window = new AdwDemoWindow(app);
            window.present();
        });
        app.run(args.length, new Strs(args));
        app.unref();
    }

    private static void showAbout(Application app) {
        final var about = new AboutWindow();
        about.setTransientFor(app.getActiveWindow());
        about.setApplicationIcon("org.gnome.Adwaita1.Demo");
        about.setApplicationName("Adwaita Demo");
        about.setDeveloperName("The GNOME Project");
        about.setVersion("Java-GTK");
        about.setWebsite("https://gitlab.gnome.org/GNOME/libadwaita");
        about.setCopyright("© 2017–2022 Purism SPC");
        about.setLicenseType(License.LGPL_2_1);
        about.setDevelopers(developers);
        about.setDesigners(designers);
        about.setArtists(designers);
        about.addLink("Documentation",  "https://gnome.pages.gitlab.gnome.org/libadwaita/doc/main/");
        about.present();
    }

    private static void showPreferences(Application app) {
        var preferences = new AdwDemoPreferencesWindow();
        preferences.setTransientFor(app.getActiveWindow());
        preferences.present();
    }

    private static void showInspector() {
        Window.setInteractiveDebugging(true);
    }
}
