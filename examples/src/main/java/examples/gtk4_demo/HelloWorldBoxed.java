package examples.gtk4_demo;

import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.ApplicationWindow;
import ch.bailu.gtk.gtk.Box;
import ch.bailu.gtk.gtk.Button;
import ch.bailu.gtk.gtk.Orientation;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;

public class HelloWorldBoxed {


    public HelloWorldBoxed(String[] args) {

        Application app = new Application(new Str("org.gtk.example"), ApplicationFlags.FLAGS_NONE);

        app.onActivate(() -> {

            var window = new ApplicationWindow(app);
            window.setTitle(new Str("Window"));
            window.setDefaultSize(200,200);

            var buttonBox = new Box(Orientation.HORIZONTAL,5);
            window.setChild(buttonBox);

            var button = Button.newWithLabelButton(new Str("Hello World"));

            button.onClicked(() -> System.out.println(new Str("Hello")));


            buttonBox.append(button);
            window.show();
        });

        app.run(args.length, new Strs(args));
    }

}
