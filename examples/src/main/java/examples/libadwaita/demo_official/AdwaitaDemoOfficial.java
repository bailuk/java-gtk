package examples.libadwaita.demo_official;

import java.io.IOException;

import ch.bailu.gtk.adw.AboutWindow;
import ch.bailu.gtk.adw.Application;
import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gio.Resource;
import ch.bailu.gtk.gtk.License;
import ch.bailu.gtk.gtk.Window;
import ch.bailu.gtk.lib.handler.action.ActionHandler;
import ch.bailu.gtk.lib.util.JavaResource;
import ch.bailu.gtk.type.Bytes;
import ch.bailu.gtk.type.Strs;
import ch.bailu.gtk.type.exception.AllocationError;

/**
 * Almost complete port of the official demo from C to Java
 * https://gitlab.gnome.org/GNOME/libadwaita/-/blob/main/demo
 *
 */
public class AdwaitaDemoOfficial {
    private static Strs developers = new Strs(new String[] {
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

    private static Strs designers = new Strs(new String[] {
            "GNOME Design Team",
            null
    });

    public static void main(String[] args) {
        loadAndRegisterGResource("/adw_demo/adwaita-demo.gresources.gresource");

        final var app = new Application("org.gnome.Adwaita1.Demo", ApplicationFlags.NON_UNIQUE);

        ActionHandler.get(app, "about").onActivate(() -> showAbout(app));
        ActionHandler.get(app, "preferences").onActivate(() -> showPreferences(app));
        ActionHandler.get(app, "inspector").onActivate(AdwaitaDemoOfficial::showInspector);

        app.onActivate(() -> {
            var window = new AdwDemoWindow(app);
            window.present();
        });
        app.run(args.length, new Strs(args));
        app.unref();
    }

    private static void loadAndRegisterGResource(String path) {
        // TODO wouldn't it be nice if resource could be loaded directly from java resources instead of .gresource?
        try (var stream = (new JavaResource(path).asStream())) {
            var bytes = new Bytes(stream.readAllBytes());
            var resource = Resource.newFromDataResource(ch.bailu.gtk.glib.Bytes.newStaticBytes(bytes, bytes.getLength()));
            resource.register();
        } catch (IOException | AllocationError e) {
            System.err.println("Load gresource failed for '"  + path + "'");
        }
    }

    private static void showAbout(Application app) {
        final var about = new AboutWindow();
        about.setTransientFor(app.getActiveWindow());
        about.setApplicationIcon("org.gnome.Adwaita1.Demo");
        about.setApplicationName("Adwaita Demo");
        about.setDeveloperName("The GNOME Project");
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
