package examples.gtk4_tutorial;

import ch.bailu.gtk.cairo.Content;
import ch.bailu.gtk.cairo.Surface;
import ch.bailu.gtk.gdk.GdkConstants;
import ch.bailu.gtk.gtk.DrawingArea;
import ch.bailu.gtk.gtk.Frame;
import ch.bailu.gtk.gtk.GestureClick;
import ch.bailu.gtk.gtk.GestureDrag;
import ch.bailu.gtk.gtk.Window;
import ch.bailu.gtk.type.Str;
import examples.DemoInterface;

/**
 * https://docs.gtk.org/gtk4/getting_started.html
 */
public class CustomDrawing implements DemoInterface {
    private static final Str TITLE = new Str("Drawing area demo");
    private static final int MARGIN = 20;

    private double startX = 0d;
    private double startY = 0d;

    private Surface surface = null;

    @Override
    public Window runDemo() {
        final var window = new Window();

        window.onDestroy(() -> {
            if (surface != null) {
                surface.destroy();
            }
        });

        window.setDefaultSize(300,300);
        var frame = new Frame(Str.NULL);
        frame.setMarginBottom(MARGIN);
        frame.setMarginTop(MARGIN);
        frame.setMarginStart(MARGIN);
        frame.setMarginEnd(MARGIN);
        window.setChild(frame);

        var drawingArea = new DrawingArea();
        frame.setChild(drawingArea);

        drawingArea.setDrawFunc((self, cr, width, height, userData) -> {
            cr.setSourceSurface(surface, 0, 0);
            cr.paint();
        }, null, data -> System.out.println("onDestroyNotify()"));

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
        drag.onDragEnd((x, y) -> drawBrush(drawingArea, startX + x, startY + y));

        var press = new GestureClick();
        press.setButton(GdkConstants.BUTTON_SECONDARY);
        drawingArea.addController(press);

        press.onPressed((n_press, x, y) -> {
            clearSurface();
            drawingArea.queueDraw();
        });

        return window;
    }

    private void clearSurface() {
        var cr = surface.createContext();
        cr.setSourceRgb(1, 1, 1);
        cr.paint();
        cr.destroy();
    }

    private void drawBrush(DrawingArea drawingArea, double x, double y) {
        var cr = surface.createContext();
        cr.rectangle(x - 3, y - 3, 6, 6);
        cr.fill();
        cr.destroy();
        drawingArea.queueDraw();
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
