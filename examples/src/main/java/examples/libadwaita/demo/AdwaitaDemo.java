package examples.libadwaita.demo;

import ch.bailu.gtk.adw.ActionRow;
import ch.bailu.gtk.adw.Application;
import ch.bailu.gtk.adw.ApplicationWindow;
import ch.bailu.gtk.adw.ColorScheme;
import ch.bailu.gtk.adw.HeaderBar;
import ch.bailu.gtk.adw.StyleManager;
import ch.bailu.gtk.adw.WindowTitle;
import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gtk.Align;
import ch.bailu.gtk.gtk.Box;
import ch.bailu.gtk.gtk.Label;
import ch.bailu.gtk.gtk.ListBox;
import ch.bailu.gtk.gtk.Orientation;
import ch.bailu.gtk.gtk.Switch;
import ch.bailu.gtk.type.Strs;

/**
 *
 * 1:1 port (Rust to Java) of libadwaita-demo
 * https://github.com/Northshore-Hero/libadwaita-demo
 * https://github.com/Northshore-Hero/libadwaita-demo/blob/main/src/main.rs
 *
 */
public class AdwaitaDemo {
    public static void main(String[] args) {
        var app = new Application("com.Libadwaita-Example", ApplicationFlags.FLAGS_NONE);

        app.onStartup(() -> StyleManager.getDefault().setColorScheme(ColorScheme.PREFER_DARK));

        app.onActivate(() -> buildUI(app));

        app.run(args.length, new Strs(args));
        app.unref();
    }

    public static void buildUI(Application app) {
        var row = new ActionRow();
        row.setTitle("Click me");
        row.setActivatable(true);
        row.setSelectable(false);
        row.onActivated(() -> System.out.println("Clicked!"));

        var row2 = new ActionRow();
        row2.setTitle("Enable Dark Mode");
        row2.setSubtitle("This row shows a switch");

        var status = StyleManager.getDefault().getDark();

        var myswitch = new Switch();
        myswitch.setValign(Align.CENTER);
        myswitch.setState(status);

        myswitch.onStateSet((boolean preferDark) -> {
           if (preferDark) {
               StyleManager.getDefault().setColorScheme(ColorScheme.PREFER_DARK);
           } else {
               StyleManager.getDefault().setColorScheme(ColorScheme.PREFER_LIGHT);
           }
           return false;
        });

        row2.addSuffix(myswitch);

        var list = new ListBox();
        list.setMarginTop(32);
        list.setMarginEnd(32);
        list.setMarginBottom(32);
        list.setMarginStart(32);
        list.addCssClass("boxed-list");

        list.append(row);
        list.append(row2);

        var mylabel = new Label("Capitalize String");
        mylabel.setMarginEnd(32);
        mylabel.setMarginTop(32);
        mylabel.setMarginStart(32);
        list.addCssClass("accent");

        // Combine the content in a box
        var content = new Box(Orientation.VERTICAL, 0);

        // Adwaitas' ApplicationWindow does not include a HeaderBar
        var headerBar = new HeaderBar();
        headerBar.setTitleWidget(new WindowTitle("First App", ""));
        content.append(headerBar);
        content.append(mylabel);
        content.append(list);

        var window = new ApplicationWindow(app);
        window.setDefaultSize(400, 400);
        window.setContent(content);
        window.present();
    }
}
