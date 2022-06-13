package examples;

import java.io.File;

import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.ApplicationWindow;
import ch.bailu.gtk.gtk.FileChooserAction;
import ch.bailu.gtk.gtk.FileChooserDialog;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;
import examples.gtk4_demo.HeaderBarSample;

public class App {

    public static Str ID = null;

    public static void main (String[] args)  {
        ID = new Str("org.gtk.example");

        //new HelloWorld(args);
        //new CustomLayoutTest(args);
        //new HeaderBarSample(args);
        //new Picker(args);
        //new LinksSample(args);
        //new Pixbufs(args);
        //new PangoTextMask(args);
        //new HelloWorldBoxed(args);
        //new ImageBridge(args);
        //new CustomDrawing(args);
        //new BuilderExample(args);
        //new ExampleApplication();
        //new Words(args);
        new HeaderBarSample(args);
        //new HugeList();
        //new AppLauncher();
        //new GlibLoop();
        //new GlibSettings();
        //new GioStreams();
        //new AdwaitaDemo(args);
        // new GeoclueSample(args);
    }

    public static File path(String path) {

        if (new File("examples").exists()) {
            return new File(path);
        }

        return new File("..", path);
    }

}
