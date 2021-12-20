package examples.gtk4_tutorial;

import ch.bailu.gtk.Callback;
import ch.bailu.gtk.cairo.Content;
import ch.bailu.gtk.cairo.Surface;
import ch.bailu.gtk.gdk.GdkConstants;
import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.ApplicationWindow;
import ch.bailu.gtk.gtk.DrawingArea;
import ch.bailu.gtk.gtk.Frame;
import ch.bailu.gtk.gtk.GestureClick;
import ch.bailu.gtk.gtk.GestureDrag;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;

/**
 * https://docs.gtk.org/gtk4/getting_started.html
 */
public class CustomDrawing {
    private double startX = 0d;
    private double startY = 0d;

    private Surface surface = null;

    public CustomDrawing(String[] args) {
        final var app = new Application(new Str("org.gtk.example"), ApplicationFlags.FLAGS_NONE);
        app.onActivate(() -> {
            final var window = new ApplicationWindow(app);

            window.setTitle(new Str("Drawing Area"));
            window.onDestroy(()->{
                if (surface != null) {
                    surface.destroy();
                }
            });

            var frame = new Frame(Str.NULL);
            window.setChild(frame);

            var drawingArea = new DrawingArea();
            drawingArea.setSizeRequest(100,100);
            frame.setChild(drawingArea);

            drawingArea.setDrawFunc((self, cr, width, height, userData) -> {
                cr.setSourceSurface(surface,0,0);
                cr.paint();
            }, new Callback.EmitterID(), null);

            drawingArea.onResize((width, height) -> {
                if (surface != null) {
                    surface.destroy();
                }
                surface = drawingArea.getNative().getSurface().createSimilarSurface(Content.COLOR, width, height);
                clearSurface();
            });

            var drag = new GestureDrag();
            drag.setButton(GdkConstants.BUTTON_PRIMARY);
            drawingArea.addController(drag);

            drag.onDragBegin((x, y) -> {
                startX = x;
                startY = y;
            });
            drag.onDragUpdate((x, y) -> drawBrush(drawingArea, startX + x, startY + y));
            drag.onDragEnd((x, y)    -> drawBrush(drawingArea, startX + x, startY + y));

            var press = new GestureClick();
            press.setButton(GdkConstants.BUTTON_SECONDARY);
            drawingArea.addController(press);

            press.onPressed((n_press, x, y) -> {
                clearSurface();
                drawingArea.queueDraw();
            });

            window.show();
        });
        app.run(args.length, new Strs(args));
        app.unref();
    }

    private void clearSurface() {
        var cr = surface.createContext();
        cr.setSourceRgb(1,1,1);
        cr.paint();
        cr.destroy();
    }

    private void drawBrush(DrawingArea drawingArea, double x, double y) {
        var cr = surface.createContext();
        cr.rectangle(x-3, y-3, 6,6);
        cr.fill();
        cr.destroy();
        drawingArea.queueDraw();
    }
}
