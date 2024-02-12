package examples.gstreamer;

import ch.bailu.gtk.gdk.Paintable;
import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gobject.Value;
import ch.bailu.gtk.gst.Element;
import ch.bailu.gtk.gst.ElementFactory;
import ch.bailu.gtk.gst.Gst;
import ch.bailu.gtk.gst.Pipeline;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.lib.util.PropertyAccess;
import ch.bailu.gtk.type.Int;
import ch.bailu.gtk.type.Pointer;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;

/**
 * <a href="https://gitlab.freedesktop.org/gstreamer/gst-plugins-rs/-/blob/main/video/gtk4/examples/gtksink.py">gtksink.py</a>
 * apt install librust-gst-plugin-gtk4-dev
 */
public class GtkSink {
    public static void main(String[] args) {
        Gst.init(Int.NULL, Pointer.NULL);

        var gtkSink = ElementFactory.make(new Str("gtk4paintablesink"), new Str("sink"));


        var paintable = new Paintable(PropertyAccess.getObjectProperty(gtkSink, "paintable").cast());
        //gtkSink.getProperty(new Str("paintable"), new Value(paintable.cast()));



        //var glContext = new Int();
        var glContext = PropertyAccess.getObjectProperty(paintable, "gl_context");

        Element source = null;
        Element glSink = null;
        Element sink = null;

        if (glContext.isNotNull()) {
            System.out.println("Using GL");
            source = ElementFactory.make(new Str("gltestsrc"), new Str("source"));
            glSink = ElementFactory.make(new Str("glsinkbin"), new Str("sink"));

            glSink.setProperty("sink", new Value(gtkSink.cast()));
            sink = glSink;
        } else {
            source = ElementFactory.make(new Str("videotestsrc"), new Str("source"));
            sink = gtkSink;
        }

        var pipeline = new Pipeline("GtkSink");

        pipeline.add(source);
        pipeline.add(sink);
        source.link(sink);

        var app = new Application("ch.bailu.gtk.examples.GtkSink", ApplicationFlags.FLAGS_NONE);
        app.onActivate(() -> {
            System.out.println("Create Window");
        });

        app.run(args.length, new Strs(args));
    }


}
