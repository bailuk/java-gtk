package examples.gstreamer;

import ch.bailu.gtk.gst.Gst;
import ch.bailu.gtk.gst.GstConstants;
import ch.bailu.gtk.gst.MessageType;
import ch.bailu.gtk.gst.State;
import ch.bailu.gtk.type.Int;
import ch.bailu.gtk.type.Pointer;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.exception.AllocationError;

/**
 * <a href="https://gstreamer.freedesktop.org/documentation/tutorials/basic/hello-world.html">Basic tutorial 1: Hello world!</a>
 */
public class HelloWorld {
    public static void main(String[] args) throws AllocationError {
        Gst.init(Int.NULL, Pointer.NULL);

        var url = new Str("playbin uri=https://gstreamer.freedesktop.org/data/media/sintel_trailer-480p.webm");
        var pipeline = Gst.parseLaunch(url);
        pipeline.setState(State.PLAYING);

        var bus = pipeline.getBus();
        var msg = bus.timedPopFiltered(GstConstants.CLOCK_TIME_NONE, MessageType.ERROR | MessageType.EOS);


        /* TODO: `Message` has a field `mini_object` with size 0. This is not supported.
        if (msg.getFieldType() == MessageType.ERROR) {
            System.err.println("An error occurred! Re-run with the GST_DEBUG=*:WARN environment variable set for more details.");
        }
        */

        msg.destroy();
        bus.unref();
        pipeline.setState(State.NULL);
        pipeline.unref();
    }
}
