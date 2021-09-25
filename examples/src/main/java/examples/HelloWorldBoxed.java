package examples;

import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.ApplicationWindow;
import ch.bailu.gtk.gtk.Button;
import ch.bailu.gtk.gtk.ButtonBox;
import ch.bailu.gtk.gtk.Orientation;
import ch.bailu.gtk.wrapper.Str;
import ch.bailu.gtk.wrapper.Strs;

public class HelloWorldBoxed {


    public HelloWorldBoxed(String[] args) {

        Application app = new Application(new Str("org.gtk.example"), ApplicationFlags.FLAGS_NONE);

        app.onActivate(() -> {

            var window = new ApplicationWindow(app);
            window.setTitle(new Str("Window"));
            window.setDefaultSize(200,200);

            var buttonBox = new ButtonBox(Orientation.HORIZONTAL);
            window.add(buttonBox);

            var button = Button.newWithLabelButton(new Str("Hello World"));

            button.onClicked(() -> System.out.println(new Str("Hello")));


            buttonBox.add(button);
            window.showAll();
        });

        app.run(args.length, new Strs(args));
    }

}
