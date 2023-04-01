
package examples;

import java.io.IOException;
import java.io.InputStream;

import ch.bailu.gtk.cairo.Context;
import ch.bailu.gtk.gdk.Gdk;
import ch.bailu.gtk.gdkpixbuf.Pixbuf;
import ch.bailu.gtk.gdkpixbuf.PixbufFormat;
import ch.bailu.gtk.gtk.DrawingArea;
import ch.bailu.gtk.gtk.Window;
import ch.bailu.gtk.lib.bridge.Image;
import ch.bailu.gtk.lib.handler.CallbackHandler;
import ch.bailu.gtk.lib.handler.SignalHandler;
import ch.bailu.gtk.lib.util.JavaResource;
import ch.bailu.gtk.type.Str;

public class ImageBridge implements DemoInterface {

    private static final Str TITLE = new Str("Image bridge");
    private static final Str DESCRIPTION = new Str("Load and display GTK logo from Java stream");

    private Pixbuf pixbuf = null;

    @Override
    public Window runDemo() {
        listSupportedFormats();

        var demoWindow = new Window();
        demoWindow.setResizable(true);
        demoWindow.setSizeRequest(300, 300);

        DrawingArea drawingArea = new DrawingArea();
        demoWindow.setChild(drawingArea);
        drawingArea.onResize(this::setPixbuf);
        drawingArea.setDrawFunc((cb, drawing_area, cr, width, height, user_data) -> drawLogo(cr), null, (cb, data)->{});

        demoWindow.onDestroy(() -> {
            CallbackHandler.unregister(drawingArea);
            SignalHandler.disconnect(demoWindow);
        });
        return demoWindow;
    }

    private void listSupportedFormats() {
        var list = Pixbuf.getFormats();

        int count = 1;
        while(list.isNotNull() && list.getFieldData().isNotNull()) {
            var format = new PixbufFormat(list.getFieldData().cast());

            System.out.println("__");
            System.out.println("Format " + count + ":");
            System.out.println(format.getName());
            System.out.println(format.getDescription());
            if (format.isDisabled()) {
                System.out.println("disabled");
            }

            if (format.isScalable()) {
                System.out.println("scalable");
            }

            if (format.isWritable()) {
                System.out.println("writeable");
            }

            list = list.getFieldNext();
            count ++;
        }
    }

    private void setPixbuf(int width, int height) {
        try {
            Pixbuf pixbufNew = loadPixbuf(width, height);
            if (pixbuf != null) {
               pixbuf.unref();
            }
            pixbuf = pixbufNew;

        } catch (IOException e) {
            System.err.println("ERROR reading image");
        }
    }


    private Pixbuf loadPixbuf(int width, int height) throws IOException {
        try (InputStream inputStream = new JavaResource("/GTK.svg").asStream()) {
            return Image.load(inputStream, width, height);
        }
    }

    private boolean drawLogo(Context cr) {
        if (pixbuf != null) {
            cr.save();
            Gdk.cairoSetSourcePixbuf(cr, pixbuf, 0, 0);
            cr.paint();
            return true;
        }
        return false;
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
