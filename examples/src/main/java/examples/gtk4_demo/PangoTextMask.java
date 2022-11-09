package examples.gtk4_demo;

import javax.annotation.Nullable;

import ch.bailu.gtk.GTK;
import ch.bailu.gtk.cairo.Context;
import ch.bailu.gtk.cairo.Pattern;
import ch.bailu.gtk.gtk.DrawingArea;
import ch.bailu.gtk.gtk.Widget;
import ch.bailu.gtk.gtk.Window;
import ch.bailu.gtk.pango.FontDescription;
import ch.bailu.gtk.pango.Layout;
import ch.bailu.gtk.pango.Pango;
import ch.bailu.gtk.pangocairo.Pangocairo;
import ch.bailu.gtk.type.Pointer;
import ch.bailu.gtk.type.Str;
import examples.DemoInterface;

public class PangoTextMask implements DemoInterface {
    private static final Str TITLE = new Str("Pango text mask");

    @Override
    public Window runDemo() {
        var window = new Window();
        window.setResizable(GTK.TRUE);
        window.setSizeRequest(400,200);

        DrawingArea da = new DrawingArea();
        window.setChild(da);
        da.setDrawFunc((cb, drawing_area, cr, width, height, user_data) -> drawText(window, cr), null, (cb, data) -> {});
        return window;
    }


    private int drawText(Widget da, Context cr) {
        cr.save();

        Layout layout = da.createPangoLayout(new Str("Pango power!\nPango power!\nPango power!"));
        FontDescription desc = Pango.fontDescriptionFromString(new Str("sans bold 34"));
        layout.setFontDescription(desc);
        desc.free();

        cr.moveTo(30,20);
        Pangocairo.layoutPath(cr,layout);
        layout.unref();

        Pattern pattern = new Pattern(0,0,da.getAllocatedWidth(), da.getAllocatedHeight());

        pattern.addColorStopRgb(0.0, 1.0, 0.0, 0.0);
        pattern.addColorStopRgb(0.2, 1.0, 0.0, 0.0);
        pattern.addColorStopRgb(0.3, 1.0, 1.0, 0.0);
        pattern.addColorStopRgb(0.4, 0.0, 1.0, 0.0);
        pattern.addColorStopRgb(0.6, 0.0, 1.0, 1.0);
        pattern.addColorStopRgb(0.7, 0.0, 0.0, 1.0);
        pattern.addColorStopRgb(0.8, 1.0, 0.0, 1.0);
        pattern.addColorStopRgb(1.0, 1.0, 0.0, 1.0);

        cr.setSource(pattern);

        cr.fillPreserve();
        pattern.destroy();

        cr.setSourceRgb(0,0,0);
        cr.setLineWidth(0.5);
        cr.stroke();
        cr.restore();
        return GTK.TRUE;

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
