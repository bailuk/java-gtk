package examples.gstreamer;

import ch.bailu.gtk.gdk.Paintable;
import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gst.Element;
import ch.bailu.gtk.gst.ElementFactory;
import ch.bailu.gtk.gst.Gst;
import ch.bailu.gtk.gst.Pipeline;
import ch.bailu.gtk.gst.State;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.ApplicationWindow;
import ch.bailu.gtk.gtk.Box;
import ch.bailu.gtk.gtk.Button;
import ch.bailu.gtk.gtk.Orientation;
import ch.bailu.gtk.gtk.Picture;
import ch.bailu.gtk.type.Int;
import ch.bailu.gtk.type.Pointer;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;

/**
 * <a href="https://gitlab.freedesktop.org/gstreamer/gst-plugins-rs/-/blob/main/video/gtk4/examples/gtksink.py">gtksink.py</a>
 */
public class GtkSink {
    private static final Str SINK = new Str("sink");
    private static final Str SOURCE = new Str("source");

    private static final Str PAINTABLE_SINK = new Str("gtk4paintablesink");
    private static final Str GL_SINK = new Str("glsinkbin");
    private static final Str GL_TEST_SRC = new Str("gltestsrc");
    private static final Str TEST_SRC = new Str("videotestsrc");
    private static final String PROPERTY_PAINTABLE = "paintable";

    private static Paintable createPaintable(Pipeline pipeline) throws Exception {
        Element source, sink;

        var gtkSink = ElementFactory.make(PAINTABLE_SINK, SINK);
        check(gtkSink, PAINTABLE_SINK);

        var paintable = new Paintable(gtkSink.getObjectProperty(PROPERTY_PAINTABLE).cast());
        check(paintable, PROPERTY_PAINTABLE);

        var glContext = paintable.getObjectProperty("gl_context");

        if (glContext.isNotNull()) {
            System.out.println("Using GL");
            source = ElementFactory.make(GL_TEST_SRC,SOURCE);
            check(source, GL_TEST_SRC);

            var glSink = ElementFactory.make(GL_SINK, SINK);
            check(glSink, GL_SINK);

            glSink.setObjectProperty(SINK.toString(), gtkSink);
            sink = glSink;
        } else {
            source = ElementFactory.make(TEST_SRC, SOURCE);
            check(source, TEST_SRC);
            sink = gtkSink;
        }

        pipeline.add(source);
        pipeline.add(sink);
        source.link(sink);
        return paintable;
    }

    private static void check(Pointer pointer, Object message) throws Exception {
        if (pointer.isNull()) {
            throw new Exception("Failed to setup '" + message + "'");
        }
    }


    public static void main(String[] args) {
        Gst.init(Int.NULL, Pointer.NULL);
        var pipeline = new Pipeline("GtkSink");
        var app = new Application("ch.bailu.gtk.examples.GtkSink", ApplicationFlags.FLAGS_NONE);

        app.onActivate(() -> {
            var win = new ApplicationWindow(app);
            var box = new Box(Orientation.VERTICAL, 0);
            var picture = new Picture();

            picture.setSizeRequest(640, 480);
            try {
                picture.setPaintable(createPaintable(pipeline));
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
            box.append(picture);

            var btn = Button.newWithLabelButton("▶/⏸");
            box.append(btn);
            btn.onClicked(()->{
                if (pipeline.getState(null, null, 1) == State.PLAYING) {
                    pipeline.setState(State.PAUSED);
                } else {
                    pipeline.setState(State.PLAYING);
                }
            });
            win.setChild(box);
            win.present();
        });

        app.run(args.length, new Strs(args));
    }

}
