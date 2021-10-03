package examples.gtk3_demo;

import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.ApplicationWindow;
import ch.bailu.gtk.gtk.Box;
import ch.bailu.gtk.gtk.Button;
import ch.bailu.gtk.gtk.ButtonBox;
import ch.bailu.gtk.gtk.ButtonBoxStyle;
import ch.bailu.gtk.gtk.Frame;
import ch.bailu.gtk.gtk.Orientation;
import ch.bailu.gtk.gtk.Widget;
import ch.bailu.gtk.gtk.Window;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;

public class ButtonBoxes {

    public ButtonBoxes(String argv[]) {
        Application app = new Application(new Str("org.gtk.example"), ApplicationFlags.FLAGS_NONE);

        app.onActivate(() -> creatButtonBox(new ApplicationWindow(app)));
        app.run(argv.length, new Strs(argv));

    }


    public Widget createBBox(boolean horizontal, String title, int spacing, int layout) {
        var frame = new Frame(new Str(title));
        ButtonBox bbox;

        if (horizontal) {
            bbox = new ButtonBox(Orientation.HORIZONTAL);
        } else {
            bbox = new ButtonBox(Orientation.VERTICAL);
        }

        bbox.setBorderWidth(5);
        frame.add(bbox);

        bbox.setLayout(layout);
        bbox.setSpacing(spacing);

        bbox.add(Button.newWithLabelButton(new Str("OK")));
        bbox.add(Button.newWithLabelButton(new Str("Cancel")));
        bbox.add(Button.newWithLabelButton(new Str("Help")));
        return frame;
    }


    public Widget creatButtonBox(Window window) {

        window.setTitle(new Str("Button Boxes"));


        window.setBorderWidth(10);

        var mainVBox = new Box(Orientation.VERTICAL,0);
        window.add(mainVBox);

        var frameHorz = new Frame(new Str("Horizontal Button Boxes"));
        mainVBox.packStart(frameHorz, 1,1,10);

        var vbox = new Box(Orientation.VERTICAL, 0);
        vbox.setBorderWidth(10);
        frameHorz.add(vbox);

        vbox.packStart(createBBox(true, "Spread", 40, ButtonBoxStyle.SPREAD), 1,1,0);
        vbox.packStart(createBBox(true, "Edge", 40, ButtonBoxStyle.EDGE), 1,1,5);
        vbox.packStart(createBBox(true, "Start", 40, ButtonBoxStyle.START), 1,1,5);
        vbox.packStart(createBBox(true, "End", 40, ButtonBoxStyle.END), 1,1,5);
        vbox.packStart(createBBox(true, "Center", 40, ButtonBoxStyle.CENTER), 1,1,5);
        vbox.packStart(createBBox(true, "Expand", 0, ButtonBoxStyle.EXPAND), 1,1,5);

        var frameVert = new Frame(new Str("Vertical Button Boxes"));
        mainVBox.packStart(frameVert, 1,1,10);

        var hbox = new Box(Orientation.HORIZONTAL, 0);
        hbox.setBorderWidth(10);
        frameVert.add(hbox);

        hbox.packStart(createBBox(false, "Spread", 10, ButtonBoxStyle.SPREAD), 1,1,0);
        hbox.packStart(createBBox(false, "Edge", 10, ButtonBoxStyle.EDGE), 1,1,5);
        hbox.packStart(createBBox(false, "Start", 10, ButtonBoxStyle.START), 1,1,5);
        hbox.packStart(createBBox(false, "End", 10, ButtonBoxStyle.END), 1,1,5);
        hbox.packStart(createBBox(false, "Center", 10, ButtonBoxStyle.CENTER), 1,1,5);
        hbox.packStart(createBBox(false, "Expand", 0, ButtonBoxStyle.EXPAND), 1,1,5);

        window.showAll();
        return window;
    }
}
