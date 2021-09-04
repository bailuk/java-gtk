package examples;

import java.io.IOException;

import ch.bailu.gtk.GTK;

public class HelloWorld {

    public static void main (String[] argv) throws IOException {
        GTK.init();
        // new HeaderBarSample(argv);
        // new ColorChooser(argv);
        // new ButtonBoxes(argv);
        // new Spinner(argv);
        // new LinksSample(argv);
        new CairoDrawingArea(argv);
    }
}
