package examples;

import java.io.IOException;

import ch.bailu.gtk.GTK;
import examples.gtk4_tutorial.CustomDrawing;
import examples.gtk4_tutorial.GtkBuilder;

public class App {

    public static void main (String[] args) throws IOException {
        GTK.init();
        //new HelloWorld(args);
        //new HeaderBarSample(args);
        //new Picker(args);
        //new LinksSample(args);
        //new Pixbufs(args);
        //new PangoTextMask(args);
        //new HelloWorldBoxed(args);
        //new ImageBridge(args);
        //new Words(args);
        //new Tutorial(args);
        //new CustomDrawing(args);
        new GtkBuilder(args);

    }
}
