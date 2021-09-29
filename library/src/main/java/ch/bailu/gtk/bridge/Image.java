package ch.bailu.gtk.bridge;

import java.io.IOException;
import java.io.InputStream;

import ch.bailu.gtk.GTK;
import ch.bailu.gtk.exception.AllocationError;
import ch.bailu.gtk.gdkpixbuf.Pixbuf;
import ch.bailu.gtk.gio.Cancellable;
import ch.bailu.gtk.gio.MemoryInputStream;

public class Image {

    public static Pixbuf load(InputStream inputStream) throws IOException, AllocationError {
        final Bytes bytes = new Bytes(inputStream.readAllBytes());
        final MemoryInputStream stream = MemoryInputStream.newFromBytesMemoryInputStream(bytes);
        final Pixbuf result = Pixbuf.newFromStreamPixbuf(stream, new Cancellable(0));

        stream.close(new Cancellable(0));
        stream.unref();
        bytes.unref();
        result.throwIfNull();
        return result;
    }

    public static Pixbuf load(InputStream inputStream, int width, int height) throws IOException, AllocationError {
        return load(inputStream, width, height, false);
    }

    public static Pixbuf load(InputStream inputStream, int width, int height, boolean preserveAspectRatio) throws IOException, AllocationError {
        final int keepAspect = GTK.is(preserveAspectRatio);
        final Bytes bytes = new Bytes(inputStream.readAllBytes());
        final MemoryInputStream stream = MemoryInputStream.newFromBytesMemoryInputStream(bytes);


        final Pixbuf result = Pixbuf.newFromStreamAtScalePixbuf(stream, width, height, keepAspect, new Cancellable(0));

        stream.close(new Cancellable(0));
        stream.unref();
        bytes.unref();
        result.throwIfNull();
        return result;
    }



}
