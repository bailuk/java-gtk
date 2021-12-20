package examples.gtk4_demo;

import java.util.Arrays;

import ch.bailu.gtk.Callback;
import ch.bailu.gtk.GTK;
import ch.bailu.gtk.gdk.Gdk;
import ch.bailu.gtk.gdk.RGBA;
import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gtk.Align;
import ch.bailu.gtk.gtk.AppChooserButton;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.ApplicationWindow;
import ch.bailu.gtk.gtk.Button;
import ch.bailu.gtk.gtk.ColorButton;
import ch.bailu.gtk.gtk.ColorChooserDialog;
import ch.bailu.gtk.gtk.DrawingArea;
import ch.bailu.gtk.gtk.FontButton;
import ch.bailu.gtk.gtk.FontChooser;
import ch.bailu.gtk.gtk.FontChooserLevel;
import ch.bailu.gtk.gtk.Grid;
import ch.bailu.gtk.gtk.Label;
import ch.bailu.gtk.gtk.ResponseType;
import ch.bailu.gtk.type.CPointer;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;

public class Picker {

    private final static String[] FONT_FAMILIES = {
        "Cursive",
        "Fantasy",
        "Monospace",
        "Sans",
        "Serif",
        "System-ui",
    };

    public Picker(String[] argv) {

        var app = new Application(new Str("org.gtk.example"), ApplicationFlags.FLAGS_NONE);
        app.onActivate(() -> colorSelection(new ApplicationWindow(app)));
        app.run(argv.length, new Strs(argv));

    }

    private void colorSelection(ApplicationWindow window) {
        window.setTitle(new Str("Pickers"));

        var table = new Grid();
        table.setMarginStart(20);
        table.setMarginEnd(20);
        table.setMarginTop(20);
        table.setMarginBottom(20);
        table.setRowSpacing(3);
        table.setColumnSpacing(10);
        window.setChild(table);

        var label = new Label(new Str("Color:"));
        label.setHalign(Align.START);
        label.setValign(Align.CENTER);
        label.setHexpand(GTK.TRUE);
        table.attach(label, 0,0,1,1);


        var picker = new ColorButton();
        table.attach(picker, 1,0,1,1);

        label = new Label(new Str("Font:"));
        label.setHalign(Align.START);
        label.setValign(Align.CENTER);
        label.setHexpand(GTK.TRUE);
        table.attach(label,0,1,1,1);

        var fontPicker = new FontButton();
        table.attach(fontPicker,1,1,1,1);

        fontPicker = new FontButton();
        var fontChooser = new FontChooser(fontPicker.getCPointerWrapper());
        fontChooser.setLevel(FontChooserLevel.FAMILY | FontChooserLevel.SIZE);

        fontChooser.setFilterFunc((family, face, data) -> {
            Str familyStr = family.getName();
            return Arrays.asList(FONT_FAMILIES).contains(familyStr.toString()) ? GTK.TRUE : GTK.FALSE;
        }, new Callback.EmitterID(), null);

        table.attach(fontPicker, 2,1,1,1);

        label = new Label(new Str("Mail:"));
        label.setHalign(Align.START);
        label.setValign(Align.CENTER);
        label.setHexpand(GTK.TRUE);

        var appPicker = new AppChooserButton(new Str("x-scheme-handler/mailto"));
        appPicker.setShowDefaultItem(GTK.TRUE);
        table.attach(label, 0,3,1,1);
        table.attach(appPicker, 1,3,1,1);

        var button = Button.newWithLabelButton(new Str("Select color"));
        table.attach(button, 0,4,1,1);
        var da = new DrawingArea();
        var rgba = new RGBA();
        rgba.setFieldRed(0f);
        rgba.setFieldBlue(1f);
        rgba.setFieldGreen(0f);
        rgba.setFieldAlpha(1f);

        da.setDrawFunc((drawing_area, cr, width, height, user_data) -> {
            Gdk.cairoSetSourceRgba(cr, rgba);
            cr.paint();
        }, new Callback.EmitterID(), null);

        table.attach(da,1,4,1,1);

        button.onClicked(() -> {
            var dialog = new ColorChooserDialog(new Str("Changing color"), window);
            dialog.setModal(1);

            dialog.onResponse(response_id -> {
                if (response_id == ResponseType.OK) {
                    var color = new ch.bailu.gtk.gtk.ColorChooser(new CPointer(dialog.getCPointer()));
                    color.getRgba(rgba);
                    da.queueDraw();
                }
                dialog.destroy();
            });
            dialog.show();

        });

        window.show();
    }


}

