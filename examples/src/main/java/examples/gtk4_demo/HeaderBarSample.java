
package examples.gtk4_demo;

import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.ApplicationWindow;
import ch.bailu.gtk.gtk.Box;
import ch.bailu.gtk.gtk.Button;
import ch.bailu.gtk.gtk.HeaderBar;
import ch.bailu.gtk.gtk.Orientation;
import ch.bailu.gtk.gtk.Switch;
import ch.bailu.gtk.gtk.TextView;
import ch.bailu.gtk.gtk.Window;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;

public class HeaderBarSample {

    public HeaderBarSample(String args[]) {
        Application app = new Application(new Str("org.gtk.example"), ApplicationFlags.FLAGS_NONE);

        app.onActivate(() -> show(new ApplicationWindow(app)));
        app.run(args.length, new Strs(args));
    }

    public void show(Window window) {

        window.setDefaultSize(600, 400);

        var header = new HeaderBar();

        window.setTitle(new Str("Welcome to GTK"));

        var button = Button.newFromIconNameButton(new Str("mail-send-receive-symbolic"));
        header.packEnd(button);

        var box = new Box(Orientation.HORIZONTAL, 0);
        box.getStyleContext().addClass(new Str("linked"));

        var button1 = Button.newFromIconNameButton(new Str("pan-start-symbolic"));
        box.append(button1);

        var button2 = Button.newFromIconNameButton(new Str("pan-end-symbolic"));
        box.append(button2);

        header.packEnd(new Switch());

        header.packStart(box);
        window.setTitlebar(header);
        window.setChild(new TextView());

        window.show();
    }
}

