package examples;

import ch.bailu.gtk.type.exception.AllocationError;
import ch.bailu.gtk.gio.DataInputStream;
import ch.bailu.gtk.gio.File;
import ch.bailu.gtk.type.Str;

/**
 *  https://www.packtpub.com/product/gnome-3-application-development-beginner-s-guide/9781849519427
 */
public class GioStreams {
    public GioStreams() {
        var file = File.newForUri(new Str("https://docs.gtk.org/gio/class.IOStream.html"));

        try {
            var stream = file.read(null);
            var inputStream = new DataInputStream(stream);

            var data = inputStream.readUpto(new Str(""),0, null,null);
            System.out.println(data.toString());

            data.destroy();
            stream.close(null);
        } catch (AllocationError allocationError) {
            allocationError.printStackTrace();
        }
    }
}
