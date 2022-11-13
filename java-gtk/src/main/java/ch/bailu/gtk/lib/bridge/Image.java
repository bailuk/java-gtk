package ch.bailu.gtk.lib.bridge;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ch.bailu.gtk.type.exception.AllocationError;
import ch.bailu.gtk.gdkpixbuf.Pixbuf;
import ch.bailu.gtk.gio.MemoryInputStream;
import ch.bailu.gtk.gio.MemoryOutputStream;
import ch.bailu.gtk.type.Str;

public class Image {

    /**
     * Loads an image from the stream into Pixbuf
     * @param inputStream stream with image data
     * @return Pixbuf
     */
    public static Pixbuf load(InputStream inputStream) throws IOException  {
        return load(inputStream, -1, -1, true);
    }

    /**
     * Loads an image from the stream into Pixbuf
     * @param inputStream stream with image data
     * @param width the width of the returned Pixbuf
     * @param height the height of the returned Pixbuf
     * @return Pixbuf
     */
    public static Pixbuf load(InputStream inputStream, int width, int height) throws IOException {
        return load(inputStream, width, height, false);
    }


    /**
     * Loads an image from the stream into Pixbuf
     * @param inputStream
     * @param width the width of the returned Pixbuf
     * @param height the height of the returned Pixbuf
     * @param preserveAspectRatio `TRUE` to preserve the image's aspect ratio
     * @return Pixbuf
     */
    public static Pixbuf load(InputStream inputStream, int width, int height, boolean preserveAspectRatio) throws IOException {
        final Bytes bytes = new Bytes(inputStream.readAllBytes());
        final MemoryInputStream stream = MemoryInputStream.newFromBytesMemoryInputStream(bytes);

        try {
            final Pixbuf result = Pixbuf.newFromStreamAtScalePixbuf(stream, width, height, preserveAspectRatio, null);
            stream.close(null);
            result.throwIfNull();
            return result;

        } catch (AllocationError e) {
            throw new IOException("Image::load::" + width + "::" + height);

        } finally {
            stream.unref();
            bytes.unref();
        }
    }


    /**
     * Convert pixbuf to an image format and write bytes to outputStream
     * @param outputStream the image will be written to this stream
     * @param pixbuf pixbuf to convert
     * @param imageFormat one of the following formats: "jpeg", "tiff", "png", "ico" or "bmp"
     */
    public static void save(OutputStream outputStream, Pixbuf pixbuf, String imageFormat) throws IOException {
        pixbuf.throwIfNull();
        try {
            final MemoryOutputStream stream = MemoryOutputStream.newResizableMemoryOutputStream();
            final Str format = new Str(imageFormat);

            if (pixbuf.saveToStreamv(stream, imageFormat, null, null, null)) {
                ch.bailu.gtk.type.Bytes bytes = new ch.bailu.gtk.type.Bytes(stream.getData().cast(), (int) stream.getDataSize());
                outputStream.write(bytes.toBytes());
            }
            format.destroy();
            stream.close(null);
            stream.unref();
        } catch (AllocationError allocationError) {
            throw new IOException("Image::save::" + imageFormat);
        }
    }
}
