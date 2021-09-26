package examples;

import java.io.IOException;

import ch.bailu.gtk.GTK;
import ch.bailu.gtk.pango.Pango;

public class App {

    public static void main (String[] argv) throws IOException {
        GTK.init();
        //new HelloWorld(argv);
        //new HeaderBarSample(argv);
        //new ColorChooser(argv);
        //new ButtonBoxes(argv);
        //new Spinner(argv);
        //new LinksSample(argv);
        //new CairoDrawingArea(argv);
        //new Pixbufs(argv);
        new PangoTextMask(argv);

        // FIXME this sample is not yet implemented
        //new PangoRotatedText(argv);
    }
}
