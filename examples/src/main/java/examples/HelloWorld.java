package examples;

import ch.bailu.gtk.GTK;

public class HelloWorld {

    public static void main (String[] argv) throws InterruptedException {
        GTK.init();
        //new HeaderBarSample(argv);
        //new ColorChooser(argv);
        //new ButtonBoxes(argv);
        new Spinner(argv);
    }
}
