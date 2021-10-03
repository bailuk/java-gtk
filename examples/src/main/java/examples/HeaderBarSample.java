package examples;

import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gio.Icon;
import ch.bailu.gtk.gio.ThemedIcon;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.ApplicationWindow;
import ch.bailu.gtk.gtk.Box;
import ch.bailu.gtk.gtk.Button;
import ch.bailu.gtk.gtk.HeaderBar;
import ch.bailu.gtk.gtk.IconSize;
import ch.bailu.gtk.gtk.Image;
import ch.bailu.gtk.gtk.Orientation;
import ch.bailu.gtk.gtk.TextView;
import ch.bailu.gtk.gtk.Window;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;

public class HeaderBarSample {

    public HeaderBarSample(String argv[]) {
        Application app = new Application(new Str("org.gtk.example"), ApplicationFlags.FLAGS_NONE);

        app.onActivate(() -> show(new ApplicationWindow(app)));
        app.run(argv.length, new Strs(argv));
    }

    public void show(Window window) {

        window.setDefaultSize(600, 400);

        var header = new HeaderBar();

        header.setShowCloseButton(1);
        header.setTitle(new Str("Welcome to GTK"));
        header.setHasSubtitle(0);

        var button = new Button();
        var icon = new ThemedIcon(new Str("mail-send-receive-symbolic"));
        var image = Image.newFromGiconImage(new Icon(icon.getCPointer()), IconSize.BUTTON);
        icon.unref();

        button.add(image);
        header.packEnd(button);

        var box = new Box(Orientation.HORIZONTAL, 0);
        box.getStyleContext().addClass(new Str("linked"));

        var button1 = new Button();
        button1.add(Image.newFromIconNameImage(new Str("pan-start-symbolic"), IconSize.BUTTON));
        box.add(button1);

        var button2 = new Button();
        button2.add(Image.newFromIconNameImage(new Str("pan-end-symbolic"), IconSize.BUTTON));
        box.add(button2);

        header.packStart(box);
        window.setTitlebar(header);
        window.add(new TextView());

        window.showAll();

    }
}
