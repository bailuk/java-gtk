
package examples.gtk4_demo;

import ch.bailu.gtk.GTK;
import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gio.Menu;
import ch.bailu.gtk.gio.MenuItem;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.ApplicationWindow;
import ch.bailu.gtk.gtk.Box;
import ch.bailu.gtk.gtk.Button;
import ch.bailu.gtk.gtk.HeaderBar;
import ch.bailu.gtk.gtk.MenuButton;
import ch.bailu.gtk.gtk.Orientation;
import ch.bailu.gtk.gtk.PopoverMenu;
import ch.bailu.gtk.gtk.Switch;
import ch.bailu.gtk.gtk.TextView;
import ch.bailu.gtk.helper.ActionHelper;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;

public class HeaderBarSample {


    public HeaderBarSample(String[] args) {
        Application app = new Application(new Str("org.gtk.example"), ApplicationFlags.FLAGS_NONE);

        var menuModel = createMenuModel(app);

        app.onActivate(() -> show(app, new ApplicationWindow(app), menuModel));
        app.run(args.length, new Strs(args));
    }

    public void show(Application app, ApplicationWindow window, Menu menuModel) {

        var header = new HeaderBar();

        window.setDefaultSize(600, 400);
        window.setTitle(new Str("Welcome to GTK"));

        var popover = PopoverMenu.newFromModelPopoverMenu(menuModel);
        var button = new MenuButton();
        button.setPopover(popover);

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

        window.setShowMenubar(GTK.TRUE);
        app.setMenubar(menuModel);

        window.show();
    }

    /**
     * https://stackoverflow.com/questions/69135934/creating-a-simple-menubar-menu-and-menu-item-in-c-using-gtk4
     */
    private Menu createMenuModel(Application app) {
        var result = new Menu();
        var networkMenu = new Menu();
        var serverMenu = new Menu();
        var radioMenu = new Menu();

        var actions = new ActionHelper(app);

        result.appendSubmenu(new Str("Network"), networkMenu);
        result.appendSubmenu(new Str("Server"), serverMenu);

        serverMenu.appendItem(new MenuItem(new Str("Connect"), new Str("app.connect")));
        serverMenu.appendItem(new MenuItem(new Str("Disconnect"), new Str("app.disconnect")));

        networkMenu.appendItem(new MenuItem(new Str("Enable"), new Str("app.toggle")));

        for (int i = 0; i< 5; i++) {
            radioMenu.appendItem(new MenuItem(new Str("eth" + i), new Str("app.select(" + i + ")")));
        }

        networkMenu.appendSection(new Str("Select device"), radioMenu);


        actions.add("connect", parameter -> System.out.println("Connect selected"));
        actions.add("disconnect", parameter -> System.out.println("Disconnect selected"));
        actions.add("toggle", true, parameter -> System.out.println("Toggle"));

        actions.add("select", 0, parameter -> System.out.println("selected " + parameter.getInt32()));

        return result;
    }
}

