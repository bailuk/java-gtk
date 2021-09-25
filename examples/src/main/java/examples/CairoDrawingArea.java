package examples;

import ch.bailu.gtk.GTK;
import ch.bailu.gtk.cairo.Content;
import ch.bailu.gtk.cairo.Context;
import ch.bailu.gtk.cairo.Surface;
import ch.bailu.gtk.gdk.EventButton;
import ch.bailu.gtk.gdk.EventConfigure;
import ch.bailu.gtk.gdk.EventMask;
import ch.bailu.gtk.gdk.EventMotion;
import ch.bailu.gtk.gdk.Gdk;
import ch.bailu.gtk.gdk.GdkConstants;
import ch.bailu.gtk.gdk.ModifierType;
import ch.bailu.gtk.gdk.Rectangle;
import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.ApplicationWindow;
import ch.bailu.gtk.gtk.Box;
import ch.bailu.gtk.gtk.DrawingArea;
import ch.bailu.gtk.gtk.Frame;
import ch.bailu.gtk.gtk.Label;
import ch.bailu.gtk.gtk.Orientation;
import ch.bailu.gtk.gtk.ShadowType;
import ch.bailu.gtk.gtk.Widget;
import ch.bailu.gtk.wrapper.Int;
import ch.bailu.gtk.wrapper.Str;
import ch.bailu.gtk.wrapper.Strs;

public class CairoDrawingArea {


    /* Pixmap for scribble area, to store current scribbles */
    private Surface surface = null;


    public CairoDrawingArea(String[] argv) {
        var app = new Application(new Str("org.gtk.example"), ApplicationFlags.FLAGS_NONE);
        app.onActivate(() -> createDrawingArea(new ApplicationWindow(app)));
        app.run(argv.length, new Strs(argv));
    }


    /* Create a new surface of the appropriate size to store our scribbles */
    private int scribbleConfigureEvent(Widget widget, EventConfigure event) {
        if (surface != null) {
            surface.destroy();
        }

        Rectangle rectangle = new Rectangle();
        widget.getAllocation(rectangle);

        surface = widget.getWindow().createSimilarSurface(Content.COLOR, rectangle.getFieldWidth(), rectangle.getFieldHeight());

        /* Initialize the surface to white */
        Context cr = surface.createContext();
        cr.setSourceRgb(1, 1, 1);
        cr.paint();
        cr.destroy();
        return GTK.TRUE;
    }


    /* Redraw the screen from the surface */
    private int drawScribble(Context cr) {
        cr.setSourceSurface(surface,0,0);
        cr.paint();
        return GTK.FALSE;
    }

    /* Draw a rectangle on the screen */
    private void drawBrush(Widget widget, double x, double y) {
        Rectangle update_rect = new Rectangle();
        Context cr;

        update_rect.setFieldX((int) (x - 3));
        update_rect.setFieldY((int) (y - 3));
        update_rect.setFieldHeight(6);
        update_rect.setFieldWidth(6);

        /* Paint to the surface, where we store our state */
        cr = surface.createContext();
        Gdk.cairoRectangle(cr, update_rect);
        cr.fill();
        cr.destroy();

        /* Now invalidate the affected region of the drawing area. */
        widget.getWindow().invalidateRect(update_rect, 0);
    }

    private int scribbleButtonPressEvent(Widget widget, EventButton event) {
        if (surface == null)
            return GTK.FALSE;
        /* paranoia check, in case we haven't gotten a configure event */

        if (event.getFieldButton() == GdkConstants.BUTTON_PRIMARY) {
            drawBrush(widget, event.getFieldX(), event.getFieldY());
        }

        /* We've handled the event, stop processing */
        return GTK.TRUE;
    }


    private final Int x = new Int();
    private final Int y = new Int();
    private final Int state = new Int();
    private int scribbleMotionNotifyEvent(Widget widget, EventMotion event) {

        if (surface == null) {
            return GTK.FALSE;
            /* paranoia check, in case we haven't gotten a configure event */
        }

        /* This call is very important; it requests the next motion event.
         * If you don't call gdk_window_get_pointer() you'll only get
         * a single motion event. The reason is that we specified
         * GDK_POINTER_MOTION_HINT_MASK to gtk_widget_set_events().
         * If we hadn't specified that, we could just use event->x, event->y
         * as the pointer location. But we'd also get deluged in events.
         * By requesting the next event as we handle the current one,
         * we avoid getting a huge number of events faster than we
         * can cope.
         */
        var window = event.getFieldWindow();
        var device = event.getFieldDevice();
        if (window.getCPointer() != 0 && device.getCPointer() != 0) {
            window.getDevicePosition(device, x, y, state);
        }

        if ((state.get() & ModifierType.BUTTON1_MASK) != 0) {
            drawBrush(widget, x.get(), y.get());
        }

        /* We've handled it, stop processing */
        return GTK.TRUE;
    }

    private int drawCheckerBoard(Widget da, Context cr) {
        int i, j, xcount, ycount, width, height;

        final int CHECK_SIZE = 10;
        final int SPACING = 2;

        /* At the start of a draw handler, a clip region has been set on
         * the Cairo context, and the contents have been cleared to the
         * widget's background color. The docs for
         * gdk_window_begin_paint_region() give more details on how this
         * works.
         */

        xcount = 0;
        width = da.getAllocatedWidth();
        height = da.getAllocatedHeight();
        i = SPACING;
        while (i < width) {
            j = SPACING;
            ycount = xcount % 2;
            /* start with even/odd depending on row */

            while (j < height) {
                if ((ycount % 2) != 0)
                    cr.setSourceRgb( 0.45777, 0, 0.45777);
                else
                    cr.setSourceRgb(1, 1, 1);


                /* If we're outside the clip, this will do nothing.
                 */

                cr.rectangle(i, j, CHECK_SIZE, CHECK_SIZE);
                cr.fill();

                j += CHECK_SIZE + SPACING;
                ++ycount;
            }

            i += CHECK_SIZE + SPACING;
            ++xcount;
        }


        /* return TRUE because we've handled this event, so no
         * further processing is required.
         */

        return GTK.TRUE;
    }

    private void close_window () {
        if (surface != null) {
            surface.destroy();
        }
        surface = null;
    }

    private void createDrawingArea(ch.bailu.gtk.gtk.Window window) {

        window.setTitle(new Str("Drawing Area"));
        window.onDestroy(()->close_window());

        window.setBorderWidth(8);

        var vbox = new Box(Orientation.VERTICAL, 8);
        vbox.setBorderWidth(8);
        window.add(vbox);

        /*
         * Create the checkerboard area
         */
        var label = new Label(Str.NULL);
        label.setMarkup(new Str("<u>Checkerboard pattern</u>"));
        vbox.packStart(label, GTK.FALSE, GTK.FALSE, 0);

        var frame = new Frame(Str.NULL);
        frame.setShadowType(ShadowType.IN);
        vbox.packStart(frame, GTK.TRUE, GTK.TRUE, 0);

        final var da = new DrawingArea();

        /* set a minimum size */
        da.setSizeRequest(100, 100);
        frame.add(da);

        da.onDraw(cr -> drawCheckerBoard(da, cr));

        /*
         * Create the scribble area
         */
        label = new Label(Str.NULL);
        label.setMarkup(new Str("<u>Scribble area</u>"));
        vbox.packStart(label, GTK.FALSE, GTK.FALSE, 0);

        frame = new Frame(Str.NULL);
        frame.setShadowType(ShadowType.IN);
        vbox.packStart(frame, GTK.TRUE, GTK.TRUE, 0);

        final var scribbleArea = new DrawingArea();

        /* set a minimum size */
        scribbleArea.setSizeRequest(100, 100);
        frame.add(scribbleArea);


        /* Signals used to handle backing surface */
        scribbleArea.onDraw(cr -> drawScribble(cr));
        scribbleArea.onConfigureEvent(event -> scribbleConfigureEvent(scribbleArea, event));

        /* Event signals */
        scribbleArea.onMotionNotifyEvent(event -> scribbleMotionNotifyEvent(scribbleArea, event));
        scribbleArea.onButtonPressEvent(event -> scribbleButtonPressEvent(scribbleArea, event));

        /* Ask to receive events the drawing area doesn't normally
         * subscribe to
         */
        scribbleArea.setEvents(da.getEvents()
                | EventMask.LEAVE_NOTIFY_MASK
                | EventMask.BUTTON_PRESS_MASK
                | EventMask.POINTER_MOTION_MASK
                | EventMask.POINTER_MOTION_HINT_MASK);

        window.showAll();
    }
}
