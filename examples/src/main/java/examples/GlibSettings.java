package examples;

import ch.bailu.gtk.gio.Settings;
import ch.bailu.gtk.gtk.Box;
import ch.bailu.gtk.gtk.Label;
import ch.bailu.gtk.gtk.Orientation;
import ch.bailu.gtk.gtk.Window;
import ch.bailu.gtk.type.Str;

/**
 * https://wiki.gnome.org/Apps/DconfEditor
 *
 *
 */
public class GlibSettings implements DemoInterface {
    private static final Str TITLE = new Str("Load settings (GlibSettings)");
    private static final Str SETTINGS = new Str("org.gnome.desktop.background");
    private static final Str ENTRY = new Str("picture-uri");

    @Override
    public Window runDemo() {
        var window = new Window();
        var box = new Box(Orientation.VERTICAL, 5);
        box.setMarginBottom(5);
        box.setMarginEnd(5);
        box.setMarginStart(5);
        box.setMarginTop(5);

        var label = new Label(SETTINGS);
        box.append(label);

        label = new Label(ENTRY);
        box.append(label);

        Str response = loadEntry();
        label = new Label(response);
        box.append(label);
        response.destroy();

        window.setChild(box);
        return window;
    }

    Str loadEntry() {
        var settings = new Settings(SETTINGS);
        if (settings.isNotNull()) {
            Str str = settings.getString(ENTRY);
            if (str.isNotNull()) {
                System.out.println(str);
                return str;
            }
        }
        return new Str("error");
    }

    @Override
    public Str getTitle() {
        return TITLE;
    }

    @Override
    public Str getDescription() {
        return TITLE;
    }
}
