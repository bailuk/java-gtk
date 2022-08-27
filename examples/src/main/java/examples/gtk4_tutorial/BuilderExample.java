package examples.gtk4_tutorial;

import java.io.IOException;

import ch.bailu.gtk.exception.AllocationError;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.Button;
import ch.bailu.gtk.gtk.Window;
import ch.bailu.gtk.helper.BuilderHelper;
import ch.bailu.gtk.type.Str;
import examples.DemoInterface;

public class BuilderExample implements DemoInterface {
    private static final Str TITLE = new Str("UI builder demo");

    private final Application application;

    public BuilderExample(Application app) {
        this.application = app;
    }

    @Override
    public Window runDemo() {

        try {
            var builder = BuilderHelper.fromResource("/builder-example/window.ui");

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
