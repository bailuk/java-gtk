package examples;

import java.io.IOException;

import ch.bailu.gtk.GTK;
import examples.gtk4_demo.HeaderBarSample;
import examples.gtk4_demo.HelloWorldBoxed;
import examples.gtk4_demo.LinksSample;
import examples.gtk4_demo.PangoTextMask;
import examples.gtk4_demo.Picker;
import examples.gtk4_demo.Pixbufs;
import examples.gtk4_tutorial.CustomDrawing;
import examples.gtk4_tutorial.BuilderExample;
import examples.gtk4_tutorial.ExampleApplication;

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
        //new CustomDrawing(args);
        //new BuilderExample(args);
        new ExampleApplication();

    }
}
