package examples;

public class HelloWorld {

    public static void main (String[] argv) throws InterruptedException {
        System.loadLibrary("glue");

        new HeaderBarSample(argv);
        //new ColorChooser(argv);
        //new ButtonBoxes(argv);
    }
}
