package examples.gtk4_demo;

import java.util.Arrays;

import ch.bailu.gtk.gdk.Gdk;
import ch.bailu.gtk.gdk.RGBA;
import ch.bailu.gtk.gtk.Align;
import ch.bailu.gtk.gtk.AppChooserButton;
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
import ch.bailu.gtk.gtk.Window;
import ch.bailu.gtk.type.PointerContainer;
import ch.bailu.gtk.type.Str;
import examples.DemoInterface;

public class Picker implements DemoInterface {

    private final static Str TITLE = new Str("Pickers");

    private final static String[] FONT_FAMILIES = {
        "Cursive",
        "Fantasy",
        "Monospace",
        "Sans",
        "Serif",
        "System-ui",
    };

    @Override
    public Window runDemo() {
        var demoWindow = new Window();
        demoWindow.setTitle(TITLE);

        var table = new Grid();
        table.setMarginStart(20);
        table.setMarginEnd(20);
        table.setMarginTop(20);
        table.setMarginBottom(20);
        table.setRowSpacing(3);
        table.setColumnSpacing(10);
        demoWindow.setChild(table);

        var label = new Label(new Str("Color:"));
        label.setHalign(Align.START);
        label.setValign(Align.CENTER);
        label.setHexpand(true);
        table.attach(label, 0,0,1,1);


        var picker = new ColorButton();
        table.attach(picker, 1,0,1,1);

        label = new Label(new Str("Font:"));
        label.setHalign(Align.START);
        label.setValign(Align.CENTER);
        label.setHexpand(true);
        table.attach(label,0,1,1,1);

        var fontPicker = new FontButton();
        table.attach(fontPicker,1,1,1,1);

        fontPicker = new FontButton();
        var fontChooser = fontPicker.asFontChooser();
        fontChooser.setLevel(FontChooserLevel.FAMILY | FontChooserLevel.SIZE);

        fontChooser.setFilterFunc((cb, family, face, data) -> {
            Str familyStr = family.getName();
            return Arrays.asList(FONT_FAMILIES).contains(familyStr.toString());
        }, null, (cb, data) -> {});

        table.attach(fontPicker, 2,1,1,1);

        label = new Label(new Str("Mail:"));
        label.setHalign(Align.START);
        label.setValign(Align.CENTER);
        label.setHexpand(true);

        var appPicker = new AppChooserButton(new Str("x-scheme-handler/mailto"));
        appPicker.setShowDefaultItem(true);
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

        da.setDrawFunc((cb, drawing_area, cr, width, height, user_data) -> {
            Gdk.cairoSetSourceRgba(cr, rgba);
            cr.paint();
        }, null, (cb, data) -> {});

        table.attach(da,1,4,1,1);

        button.onClicked(() -> {
            var dialog = new ColorChooserDialog(new Str("Changing color"), demoWindow);
            dialog.setModal(true);

            dialog.onResponse(response_id -> {
                if (response_id == ResponseType.OK) {
                    var color = dialog.asColorChooser();
                    color.getRgba(rgba);
                    da.queueDraw();
                }
                dialog.destroy();
            });
            dialog.show();

        });
        return demoWindow;
    }


    @Override
    public Str getTitle() {
        return TITLE;
    }

    @Override
    public Str getDescription() {
        return TITLE;
    }
}
