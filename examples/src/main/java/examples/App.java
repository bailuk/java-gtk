package examples;

import java.io.File;

import ch.bailu.gtk.gtk.HeaderBar;
import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.ApplicationWindow;
import ch.bailu.gtk.gtk.Box;
import ch.bailu.gtk.gtk.Button;
import ch.bailu.gtk.gtk.Label;
import ch.bailu.gtk.gtk.Orientation;
import ch.bailu.gtk.gtk.ScrolledWindow;
import ch.bailu.gtk.gtk.Window;
import ch.bailu.gtk.lib.handler.CallbackHandler;
import ch.bailu.gtk.lib.handler.ClassHandler;
import ch.bailu.gtk.lib.handler.SignalHandler;
import ch.bailu.gtk.lib.handler.action.ActionHandler;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;
import examples.gtk4_demo.AppLauncher;
import examples.gtk4_demo.Accordion;
import examples.gtk4_demo.HeaderBarSample;
import examples.gtk4_demo.HelloWorldBoxed;
import examples.gtk4_demo.LinksSample;
import examples.gtk4_demo.PangoTextMask;
import examples.gtk4_demo.Picker;
import examples.gtk4_demo.Pixbufs;
import examples.gtk4_tutorial.UiBuilderExample;
import examples.gtk4_tutorial.CustomDrawing;
import examples.gtk4_tutorial.ExampleApplication;
import examples.test.MultiThreadingCallbacks;

public class App {

    public final static String ID = "org.gtk.example";
    public final static int WIDTH = 400;
    public final static int HEIGHT = 900;

    public static void main (String[] args)  {

        System.setProperty("jna.debug_load", "true");

        printProperty("java.library.path");
        printProperty("jna.library.path");

        var app = new Application(ID, ApplicationFlags.FLAGS_NONE);
        app.onActivate(()->{
            var demoList = new Box(Orientation.VERTICAL, 0);

            var scrolled = new ScrolledWindow();
            scrolled.setChild(demoList);

            var window = new ApplicationWindow(app);
            window.setTitle("java-gtk demo");
            var headerBar = new HeaderBar();
            var dumpResources = Button.newWithLabelButton("Dump resources");
            dumpResources.onClicked(() -> {
                ActionHandler.dump(System.out);
                ClassHandler.dump(System.out);
                CallbackHandler.dump(System.out);
                SignalHandler.dump((System.out));
            });
            headerBar.packStart(dumpResources);
            window.setTitlebar(headerBar);
            window.setDefaultSize(WIDTH, HEIGHT);
            window.setChild(scrolled);

            addSample(demoList, window, new ImageBridge());
            addSample(demoList, window, new AppLauncher());
            addSample(demoList, window, new Pixbufs());
            addSample(demoList, window, new Picker());
            addSample(demoList, window, new HeaderBarSample(app));
            addSample(demoList, window, new LinksSample());
            addSample(demoList, window, new PangoTextMask());
            addSample(demoList, window, new HelloWorldBoxed());
            addSample(demoList, window, new CustomDrawing());
            addSample(demoList, window, new UiBuilderExample(app));
            addSample(demoList, window, new HugeList(app));
            addSample(demoList, window, new ExampleApplication(app));
            addSample(demoList, window, new GlibSettings());
            addSample(demoList, window, new MultiThreadingCallbacks());
            addSample(demoList, window, new Accordion());

            window.show();
        });

        System.exit(app.run(args.length, new Strs(args)));
    }


    private static void printProperty(String property) {
        System.out.print(property);
        System.out.print(": ");
        System.out.println(System.getProperty(property));
    }

    private static void addSample(Box demoList, Window window, DemoInterface demo) {
        var entry = new Box(Orientation.HORIZONTAL,0);
        var label = new Label(demo.getTitle());
        var button = new Button();

        entry.setMarginEnd(10);
        entry.setMarginTop(10);
        entry.setMarginStart(10);
        entry.setMarginBottom(10);
        label.setHexpand(true);
        label.setXalign(0);
        button.setLabel(new Str("Run"));
        button.onClicked(() -> runDemo(demo, window));
        entry.append(label);
        entry.append(button);
        demoList.append(entry);
    }


    private static void runDemo(DemoInterface demo, Window parent) {
        Window demoWindow =  demo.runDemo();
        demoWindow.setDisplay(parent.getDisplay());
        demoWindow.setTitle(demo.getTitle());
        demoWindow.setTransientFor(parent);
        demoWindow.setModal(true);
        demoWindow.show();
    }

    public static File path(String path) {
        if (new File("examples").exists()) {
            return new File(path);
        }
        return new File("..", path);
    }
}
