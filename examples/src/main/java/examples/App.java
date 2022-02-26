package examples;

import java.io.File;
import java.io.IOException;

import ch.bailu.gtk.GTK;
import ch.bailu.gtk.gtk.Gtk;
import ch.bailu.gtk.type.Str;

public class App {

    public static Str ID = null;

    public static void main (String[] args) throws IOException {
        GTK.init();
        ID = new Str("org.gtk.example");

        Gtk.init();
        //new HelloWorld(args);
        //new CustomLayoutTest(args);
        //new HeaderBarSample(args);
        //new Picker(args);
        //new LinksSample(args);
        //new Pixbufs(args);
        //new PangoTextMask(args);
        //new HelloWorldBoxed(args);
        new ImageBridge(args);
        //new CustomDrawing(args);
        //new BuilderExample(args);
        //new ExampleApplication();
        //new Words(args);
        //new HugeList();
        //new AppLauncher();
        //new GlibLoop();
        //new GlibSettings();
        //new GioStreams();
        //new AdwaitaDemo(args);
    }

    public static File path(String path) {

        if (new File("examples").exists()) {
            return new File(path);
        }

        return new File("..", path);
    }

}
