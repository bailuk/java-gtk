package examples.gtk4_tutorial;

import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.ApplicationWindow;
import ch.bailu.gtk.gtk.Button;
import ch.bailu.gtk.gtk.Grid;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;

/**
 * https://docs.gtk.org/gtk4/getting_started.html
 */
public class HelloWorld {

    public HelloWorld(String[] args) {
        final var app = new Application(new Str("org.gtk.example"), ApplicationFlags.FLAGS_NONE);
        app.onActivate(() -> {
            final var window = new ApplicationWindow(app);

            window.setTitle(new Str("Window"));
            window.setDefaultSize(200,200);

            var grid = new Grid();
            window.setChild(grid);

            var buttonHello = Button.newWithLabelButton(new Str("Hello"));
            buttonHello.onClicked(()->System.out.println("Hello"));
            grid.attach(buttonHello,0,0,1,1);

            var buttonWorld = Button.newWithLabelButton(new Str("World"));
            buttonWorld.onClicked(()->System.out.println("World"));
            grid.attach(buttonWorld,1,0,1,1);

            var buttonQuit = Button.newWithLabelButton(new Str("Quit"));
            buttonQuit.onClicked(()->window.destroy());
            grid.attach(buttonQuit, 0,1,2,1);

            window.show();
        });
        app.run(args.length, new Strs(args));
        app.unref();
    }
}
