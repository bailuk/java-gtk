package examples;

import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.ApplicationWindow;
import ch.bailu.gtk.gtk.Button;
import ch.bailu.gtk.gtk.ButtonBox;
import ch.bailu.gtk.gtk.Orientation;

public class HelloWorldBoxed {


    public HelloWorldBoxed(String[] args) {

        Application app = new Application("org.gtk.example", ApplicationFlags.FLAGS_NONE);

        app.onActivate(() -> {

            var window = new ApplicationWindow(app);
            window.setTitle("Window");
            window.setDefaultSize(200,200);

            var buttonBox = new ButtonBox(Orientation.HORIZONTAL);
            window.add(buttonBox);

            var button = Button.newWithLabelButton("Hello World");

            button.onClicked(() -> System.out.println("Hello"));


            buttonBox.add(button);
            window.showAll();
        });

        app.run(args.length, args);
    }

}
