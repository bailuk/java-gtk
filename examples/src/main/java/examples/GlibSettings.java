package examples;

import ch.bailu.gtk.gio.Settings;
import ch.bailu.gtk.type.Str;

/**
 * https://wiki.gnome.org/Apps/DconfEditor
 *
 *
 */
public class GlibSettings {
    public GlibSettings() {
        var settings = new Settings(new Str("org.gnome.desktop.background"));
        if (settings.isNotNull()) {
            Str str = settings.getString(new Str("picture-uri"));
            if (str.isNotNull()) {
                System.out.println(str.toString());
                str.destroy();
            }
        }
    }
}
