package examples;

import java.io.IOException;

import ch.bailu.gtk.GTK;
import ch.bailu.gtk.gdk.GdkConstants;
import ch.bailu.gtk.gdk.Toplevel;
import ch.bailu.gtk.glib.Glib;
import ch.bailu.gtk.glib.MainLoop;
import ch.bailu.gtk.gtk.Gtk;
import ch.bailu.gtk.gtk.GtkConstants;
import ch.bailu.gtk.gtk.Label;
import ch.bailu.gtk.gtk.Widget;
import ch.bailu.gtk.gtk.Window;
import ch.bailu.gtk.type.Str;
import examples.gtk4_demo.HeaderBarSample;
import examples.gtk4_demo.HelloWorldBoxed;
import examples.libadwaita_demo.AdwaitaDemo;

public class App {

    public static Str ID = null;

    public static void main (String[] args) throws IOException {
        GTK.init();
        ID = new Str("org.gtk.example");

        Gtk.init();
        new HelloWorldBoxed(args);


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
        //new HugeList();
        //new AppLauncher();
        //new GlibLoop();
        //new GlibSettings();
        //new GioStreams();
        //new AdwaitaDemo(args);
    }
}
