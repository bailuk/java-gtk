package examples;

import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gtk.Align;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.ApplicationWindow;
import ch.bailu.gtk.gtk.Box;
import ch.bailu.gtk.gtk.Button;
import ch.bailu.gtk.gtk.ColorChooserDialog;
import ch.bailu.gtk.gtk.Dialog;
import ch.bailu.gtk.gtk.DrawingArea;
import ch.bailu.gtk.gtk.Frame;
import ch.bailu.gtk.gtk.Orientation;
import ch.bailu.gtk.gtk.ShadowType;

public class ColorChooser {
    public ColorChooser(String[] argv) {

        Application app = new Application("org.gtk.example", ApplicationFlags.FLAGS_NONE);
        app.onActivate(() -> colorSelection(new ApplicationWindow(app)));
        app.run(argv.length, argv);

    }

    private void colorSelection(ApplicationWindow window) {


        window.setBorderWidth(8);;
        var vbox = new Box(Orientation.VERTICAL, 8);

        vbox.setBorderWidth(8);
        window.add(vbox);


        var frame = new Frame("");
        frame.setShadowType(ShadowType.IN);
        vbox.packStart(frame, 1,1,0);

        var da = new DrawingArea();
/*
        da.onDraw(() -> {

        });
*/
        da.setSizeRequest(200,200);

        var button = Button.newWithMnemonicButton("_Change the above color");
        button.setHalign(Align.END);
        button.setValign(Align.CENTER);

        vbox.packStart(button, 0,0,0);

        button.onClicked(() -> {
            var dialog = new ColorChooserDialog("Chnaging color", window);
            dialog.setModal(1);

            dialog.onResponse(response_id -> {

            });
            dialog.showAll();
        });

        window.showAll();
    }


}
