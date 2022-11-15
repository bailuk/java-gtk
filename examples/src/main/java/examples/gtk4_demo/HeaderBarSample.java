
package examples.gtk4_demo;

import ch.bailu.gtk.gio.Menu;
import ch.bailu.gtk.gio.MenuItem;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.Box;
import ch.bailu.gtk.gtk.Button;
import ch.bailu.gtk.gtk.HeaderBar;
import ch.bailu.gtk.gtk.MenuButton;
import ch.bailu.gtk.gtk.Orientation;
import ch.bailu.gtk.gtk.Switch;
import ch.bailu.gtk.gtk.TextView;
import ch.bailu.gtk.gtk.Window;
import ch.bailu.gtk.lib.handler.action.ActionHandler;
import ch.bailu.gtk.type.Str;
import examples.DemoInterface;

public class HeaderBarSample implements DemoInterface {

    private static final Str TITLE = new Str("Header bar demo");

    private final Application app;

    public HeaderBarSample(Application app) {
        this.app = app;
    }

    @Override
    public Window runDemo() {
        var window = new Window();
        var header = new HeaderBar();

        window.setApplication(app); // Menu actions only work if application is set
        window.setDefaultSize(600, 400);
        window.setTitle(TITLE);

        var button = new MenuButton();
        button.setMenuModel(createMenuModel(app));
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

        return window;
    }

    /**
     * https://stackoverflow.com/questions/69135934/creating-a-simple-menubar-menu-and-menu-item-in-c-using-gtk4
     */
    private Menu createMenuModel(Application app) {
        var result = new Menu();
        var networkMenu = new Menu();
        var serverMenu = new Menu();
        var radioMenu = new Menu();

        result.appendSubmenu(new Str("Network"), networkMenu);
        result.appendSubmenu(new Str("Server"), serverMenu);

        serverMenu.appendItem(new MenuItem(new Str("Connect"), new Str("app.connect")));
        serverMenu.appendItem(new MenuItem(new Str("Disconnect"), new Str("app.disconnect")));

        networkMenu.appendItem(new MenuItem(new Str("Enable"), new Str("app.toggle")));

        for (int i = 0; i< 5; i++) {
            radioMenu.appendItem(new MenuItem(new Str("eth" + i), new Str("app.select(" + i + ")")));
        }

        networkMenu.appendSection(new Str("Select device"), radioMenu);

        ActionHandler.get(app, "connect").onActivate(()-> System.out.println("Connect selected"));
        ActionHandler.get(app, "disconnect").onActivate(()-> System.out.println("Disconnect selected"));
        ActionHandler.get(app,"toggle", true).onToggle((boolean value) -> System.out.println("Enabled: " + value));
        ActionHandler.get(app,"select", 0).onChange((int value) -> System.out.println("Selected: " + value));

        return result;
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
