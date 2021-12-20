package examples;

import java.io.IOException;

import ch.bailu.gtk.GTK;
import examples.gtk4_demo.Pixbufs;

public class App {

    public static void main (String[] argv) throws IOException {
        GTK.init();
        //new HelloWorld(argv);
        //new HeaderBarSample(argv);
        //new Picker(argv);
        //new LinksSample(argv);
        new Pixbufs(argv);
        //new PangoTextMask(argv);
        //new HelloWorldBoxed(argv);



        //new ImageBridge(argv);
    }
}
