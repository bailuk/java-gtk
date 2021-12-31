package examples;

import java.io.IOException;

import ch.bailu.gtk.GTK;
import ch.bailu.gtk.type.Str;
import examples.gtk4_demo.Pixbufs;

public class App {

    public static Str ID = null;

    public static void main (String[] args) throws IOException {
        GTK.init();
        ID = new Str("org.gtk.example");

        //new HelloWorld(args);
        //new HeaderBarSample(args);
        //new Picker(args);
        //new LinksSample(args);
        new Pixbufs(args);
        //new PangoTextMask(args);
        //new HelloWorldBoxed(args);
        //new ImageBridge(args);
        //new CustomDrawing(args);
        //new BuilderExample(args);
        //new ExampleApplication();
        //new Words(args);
        //new HugeList();
        //new AppLauncher();
        //new GlibLoop();
        //new GlibSettings();
        //new GioStreams();
    }
}
