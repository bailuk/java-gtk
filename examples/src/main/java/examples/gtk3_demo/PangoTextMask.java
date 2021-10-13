package examples.gtk3_demo;

import ch.bailu.gtk.GTK;
import ch.bailu.gtk.cairo.Context;
import ch.bailu.gtk.cairo.Matrix;
import ch.bailu.gtk.cairo.Pattern;
import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.ApplicationWindow;
import ch.bailu.gtk.gtk.DrawingArea;
import ch.bailu.gtk.gtk.Widget;
import ch.bailu.gtk.pango.FontDescription;
import ch.bailu.gtk.pango.Layout;
import ch.bailu.gtk.pango.Pango;
import ch.bailu.gtk.pangocairo.Pangocairo;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;

public class PangoTextMask {
    public PangoTextMask(String[] argv) {

        var app = new Application(new Str("org.gtk.example"), ApplicationFlags.FLAGS_NONE);
        app.onActivate(() -> doTextmask(new ApplicationWindow(app)));
        app.run(argv.length, new Strs(argv));

    }

    void doTextmask(ApplicationWindow window) {
        window.setResizable(GTK.TRUE);
        window.setSizeRequest(400,200);
        window.setTitle(new Str("Text Mask"));

        DrawingArea da = new DrawingArea();
        window.add(da);
        da.onDraw(cr -> drawText(window, cr));
        window.showAll();
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

}
