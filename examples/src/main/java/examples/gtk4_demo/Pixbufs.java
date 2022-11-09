package examples.gtk4_demo;

import java.io.IOException;
import java.io.InputStream;

import ch.bailu.gtk.bridge.Image;
import ch.bailu.gtk.cairo.Context;
import ch.bailu.gtk.exception.AllocationError;
import ch.bailu.gtk.gdk.FrameClock;
import ch.bailu.gtk.gdk.Gdk;
import ch.bailu.gtk.gdk.Rectangle;
import ch.bailu.gtk.gdkpixbuf.Colorspace;
import ch.bailu.gtk.gdkpixbuf.InterpType;
import ch.bailu.gtk.gdkpixbuf.Pixbuf;
import ch.bailu.gtk.glib.GlibConstants;
import ch.bailu.gtk.gtk.ButtonsType;
import ch.bailu.gtk.gtk.DialogFlags;
import ch.bailu.gtk.gtk.DrawingArea;
import ch.bailu.gtk.gtk.MessageDialog;
import ch.bailu.gtk.gtk.MessageType;
import ch.bailu.gtk.gtk.Window;
import ch.bailu.gtk.lib.resources.JavaResource;
import ch.bailu.gtk.type.Str;
import examples.DemoInterface;

public class Pixbufs implements DemoInterface {
    private final static Str TITLE = new Str("Pixbufs");
    private final static Str DESCRIPTION = new Str(
            "A GdkPixbuf represents an image, normally in RGB or RGBA format.\n" +
            "Pixbufs are normally used to load files from disk and perform image scaling.\n" +
            "This demo is not all that educational, but looks cool.\n" +
            "It was written by Extreme Pixbuf Hacker Federico Mena Quintero.\n" +
            "It also shows off how to use GtkDrawingArea to do a simple animation.\n" +
            "Look at the Image demo for additional pixbuf usage examples.\n");

    private static final String BACKGROUND_NAME = "/pixbufs/background.jpg";

    private static final String[] IMAGE_NAMES = {
            "/pixbufs/apple-red.png",
            "/pixbufs/gnome-applets.png",
            "/pixbufs/gnome-calendar.png",
            "/pixbufs/gnome-foot.png",
            "/pixbufs/gnome-gmush.png",
            "/pixbufs/gnome-gimp.png",
            "/pixbufs/gnome-gsame.png",
            "/pixbufs/gnu-keys.png"
    };

    /* Current frame */
    private Pixbuf frame;

    /* Background image */
    private Pixbuf background;
    private int backWidth, backHeight;

    /* Images */
    private final Pixbuf[] images = new Pixbuf[IMAGE_NAMES.length];

    /* Widgets */
    private DrawingArea da;

    private static final long CYCLE_TIME = 3000000; /* 3 seconds */
    private long startTime;


    void loadPixbufs() throws AllocationError, IOException {
        background = loadPixbuf(BACKGROUND_NAME);

        backWidth = background.getWidth();
        backHeight = background.getHeight();

        for (int i = 0; i < images.length; i++) {
            images[i] = loadPixbuf(IMAGE_NAMES[i]);
        }
    }

    Pixbuf loadPixbuf(String resourcePath) throws IOException {
        try (InputStream inputStream = new JavaResource(resourcePath).asStream()) {
            return Image.load(inputStream);
        }
    }

    boolean onDraw(Context cr) {
        Gdk.cairoSetSourcePixbuf(cr, frame, 0, 0);
        cr.paint();
        return true;
    }


    // TODO implement free() or destroy():
    private Rectangle r1 = new Rectangle(), r2 = new Rectangle(), dest = new Rectangle();

    private boolean onTick(FrameClock frameClock) {
        long currentTime;
        double f;
        double xmid, ymid;
        double radius;

        background.copyArea(0,0, backWidth, backHeight, frame, 0,0);

        if (startTime == 0) {
            startTime = frameClock.getFrameTime();
        }

        currentTime = frameClock.getFrameTime();
        f = ((currentTime - startTime) % CYCLE_TIME) / (double)CYCLE_TIME;

        xmid = backWidth / 2.0;
        ymid = backHeight / 2.0;

        radius = Math.min(xmid, ymid) / 2.0;

        for (int i = 0; i< images.length; i++) {
            double ang;
            int xpos, ypos;
            int iw, ih;
            double r;
            double k;

            ang = 2.0 * Math.PI * (double) i / images.length - f * 2.0 * Math.PI;

            iw = images[i].getWidth();
            ih = images[i].getHeight();

            r = radius + (radius / 3.0) * Math.sin(f * 2.0 * Math.PI);

            xpos = (int) Math.floor(xmid + r * Math.cos(ang) - iw / 2.0 + 0.5);
            ypos = (int) Math.floor(ymid + r * Math.sin(ang) - ih / 2.0 + 0.5);


            int overallAlpha;
            if ((i & 1) != 0) {
                k = Math.sin(f * 2.0 * Math.PI);
                overallAlpha = (int) Math.max(127, Math.abs(255 * Math.sin(f * 2.0 * Math.PI)));
            } else {
                k = Math.cos(f * 2.0 * Math.PI);
                overallAlpha = (int) Math.max(127, Math.abs(255 * Math.cos(f * 2.0 * Math.PI)));
            }

            k = 2.0 * k * k;
            k = Math.max(0.25, k);


            r1.setFieldX(xpos);
            r1.setFieldY(ypos);
            r1.setFieldWidth((int) (iw * k));
            r1.setFieldHeight((int) (ih * k));

            r2.setFieldX(0);
            r2.setFieldY(0);
            r2.setFieldWidth(backWidth);
            r2.setFieldHeight(backHeight);

            if (r1.intersect(r2, dest)) {
                images[i].composite(frame,
                        dest.getFieldX(),
                        dest.getFieldY(),
                        dest.getFieldWidth(),
                        dest.getFieldHeight(),
                        xpos, ypos,
                        k, k,
                        InterpType.NEAREST,
                        overallAlpha);
            }
        }
        da.queueDraw();
        return GlibConstants.SOURCE_CONTINUE;
    }

    @Override
    public Window runDemo() {
        var demoWindow = new Window();
        demoWindow.setResizable(false);

        try  {
            loadPixbufs();
            frame = new Pixbuf(Colorspace.RGB, false, 8, backWidth, backHeight);
            da = new DrawingArea();


            da.setDrawFunc((cb, drawingArea, cr, width, height, userData) -> onDraw(cr), null, (cb, data) -> {});
            demoWindow.setChild(da);
            demoWindow.setSizeRequest(backWidth, backHeight);
            demoWindow.addTickCallback((cb, widget, frame_clock, user_data) -> onTick(frame_clock), null, (cb, data) -> {});

        } catch (AllocationError | IOException e) {
            System.out.println(e.getMessage());
            var dialog = new MessageDialog(
                    demoWindow,
                    DialogFlags.DESTROY_WITH_PARENT,
                    MessageType.ERROR,
                    ButtonsType.CLOSE,
                    new Str("Failed to load an image: " + e.getMessage()));

            dialog.onResponse(response_id -> demoWindow.destroy());
            dialog.show();
        }
        return demoWindow;
    }

    @Override
    public Str getTitle() {
        return TITLE;
    }

    @Override
    public Str getDescription() {
        return DESCRIPTION;
    }
}
