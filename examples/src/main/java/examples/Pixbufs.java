package examples;

import ch.bailu.gtk.GTK;
import ch.bailu.gtk.cairo.Context;
import ch.bailu.gtk.exception.AllocationError;
import ch.bailu.gtk.gdk.FrameClock;
import ch.bailu.gtk.gdk.Gdk;
import ch.bailu.gtk.gdk.Rectangle;
import ch.bailu.gtk.gdkpixbuf.Colorspace;
import ch.bailu.gtk.gdkpixbuf.InterpType;
import ch.bailu.gtk.gdkpixbuf.Pixbuf;
import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.glib.GlibConstants;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.ApplicationWindow;
import ch.bailu.gtk.gtk.ButtonsType;
import ch.bailu.gtk.gtk.DialogFlags;
import ch.bailu.gtk.gtk.DrawingArea;
import ch.bailu.gtk.gtk.MessageDialog;
import ch.bailu.gtk.gtk.MessageType;
import ch.bailu.gtk.gtk.Widget;
import ch.bailu.gtk.wrapper.Str;

/*
    Pixbufs
    A GdkPixbuf represents an image, normally in RGB or RGBA format. Pixbufs are normally used to load files from disk and perform image scaling.
    This demo is not all that educational, but looks cool. It was written by Extreme Pixbuf Hacker Federico Mena Quintero. It also shows off how to use GtkDrawingArea to do a simple animation.
    Look at the Image demo for additional pixbuf usage examples.
*/
public class Pixbufs {
    private static final String BACKGROUND_NAME = "src/main/resources/background.jpg";

    private static final String IMAGE_NAMES[] = {
            "src/main/resources/apple-red.png",
            "src/main/resources/gnome-applets.png",
            "src/main/resources/gnome-calendar.png",
            "src/main/resources/gnome-foot.png",
            "src/main/resources/gnome-gmush.png",
            "src/main/resources/gnome-gimp.png",
            "src/main/resources/gnome-gsame.png",
            "src/main/resources/gnu-keys.png"
    };

    /* Current frame */
    private Pixbuf frame;

    /* Background image */
    private Pixbuf background;
    private int backWidth, backHeight;

    /* Images */
    private final Pixbuf images[] = new Pixbuf[IMAGE_NAMES.length];

    /* Widgets */
    private Widget da;

    private static final long CYCLE_TIME = 3000000; /* 3 seconds */
    private long startTime;


    public Pixbufs(String[] argv) {
        var app = new Application(new Str("org.gtk.example"), ApplicationFlags.FLAGS_NONE);
        app.onActivate(() -> doPixbufs(new ApplicationWindow(app)));
        app.run(argv.length, argv);


    }

    void loadPixbufs() throws AllocationError {
        background = Pixbuf.newFromFilePixbuf(new Str(BACKGROUND_NAME));

        backWidth = background.getWidth();
        backHeight = background.getHeight();

        for (int i = 0; i < images.length; i++) {
            images[i] = Pixbuf.newFromFilePixbuf(new Str(IMAGE_NAMES[i]));
        }
    }

    int onDraw(Widget widget, Context cr) {
        Gdk.cairoSetSourcePixbuf(cr, frame, 0, 0);
        cr.paint();
        return GTK.TRUE;
    }


    // TODO implement free() or destroy():
    private Rectangle r1 = new Rectangle(), r2 = new Rectangle(), dest = new Rectangle();

    private int onTick(FrameClock frameClock) {
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

            if (r1.intersect(r2, dest) == GTK.TRUE) {
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

    private Widget doPixbufs(ApplicationWindow window) {
        window.setTitle(new Str("Pixbufs"));
        window.setResizable(GTK.FALSE);
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        try  {
            loadPixbufs();

            frame = new Pixbuf(Colorspace.RGB, GTK.FALSE, 8, backWidth, backHeight);
            da = new DrawingArea();

            da.onDraw(cr -> onDraw(da, cr));
            window.add(da);
            window.setSizeRequest(backWidth, backHeight);
            window.addTickCallback((widget, frame_clock, user_data) -> onTick(frame_clock), 0, data -> {});

        } catch (AllocationError e) {
            System.out.println(e.getMessage());
            var dialog = new MessageDialog(
                    window,
                    DialogFlags.DESTROY_WITH_PARENT,
                    MessageType.ERROR,
                    ButtonsType.CLOSE,
                    new Str("Failed to load an image: " + e.getMessage()));

            dialog.onResponse(response_id -> window.destroy());
            dialog.show();
        }
        window.showAll();
        return window;
    }
}
