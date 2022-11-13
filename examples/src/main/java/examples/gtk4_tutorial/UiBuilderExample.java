package examples.gtk4_tutorial;

import java.io.IOException;

import ch.bailu.gtk.type.exception.AllocationError;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.Button;
import ch.bailu.gtk.gtk.Window;
import ch.bailu.gtk.lib.bridge.UiBuilder;
import ch.bailu.gtk.type.Str;
import examples.DemoInterface;

public class UiBuilderExample implements DemoInterface {
    private static final Str TITLE = new Str("UI builder demo");

    private final Application application;

    public UiBuilderExample(Application app) {
        this.application = app;
    }

    @Override
    public Window runDemo() {

        try {
            var builder = UiBuilder.fromResource("/builder-example/window.ui");

            var window = new Window(builder.getObject("window"));
            window.setApplication(application);

            var button1 = new Button(builder.getObject("button1"));
            button1.onClicked(() -> System.out.println("Hello from button1"));

            var button2 = new Button(builder.getObject("button2"));
            button2.onClicked(() -> System.out.println("Hello from button2"));

            var quit = new Button(builder.getObject("quit"));
            quit.onClicked(window::close);

            return window;
        } catch (AllocationError | IOException e) {
            System.err.println(e.getMessage());

        }
        return new Window();
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
