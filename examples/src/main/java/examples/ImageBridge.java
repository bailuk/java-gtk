
package examples;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nullable;

import ch.bailu.gtk.GTK;
import ch.bailu.gtk.bridge.Image;
import ch.bailu.gtk.cairo.Context;
import ch.bailu.gtk.gdk.Gdk;
import ch.bailu.gtk.gdkpixbuf.Pixbuf;
import ch.bailu.gtk.gdkpixbuf.PixbufFormat;
import ch.bailu.gtk.gtk.DrawingArea;
import ch.bailu.gtk.gtk.Window;
import ch.bailu.gtk.lib.resources.JavaResource;
import ch.bailu.gtk.type.CPointer;
import ch.bailu.gtk.type.Pointer;
import ch.bailu.gtk.type.Str;

public class ImageBridge implements DemoInterface {

    private static final Str TITLE = new Str("Image bridge");
    private static final Str DESCRIPTION = new Str("Load and display GTK logo from Java stream");

    private Pixbuf pixbuf = null;

    @Override
    public Window runDemo() {
        listSupportedFormats();

        var demoWindow = new Window();
        demoWindow.setResizable(GTK.TRUE);
        demoWindow.setSizeRequest(App.WIDTH, App.HEIGHT);

        DrawingArea drawingArea = new DrawingArea();
        demoWindow.setChild(drawingArea);
        drawingArea.onResize(this::setPixbuf);
        drawingArea.setDrawFunc((cb, drawing_area, cr, width, height, user_data) -> drawLogo(cr), null, (cb, data) -> {});
        return demoWindow;
    }

    private void listSupportedFormats() {
        var list = Pixbuf.getFormats();

        int count = 1;
        while(list.isNotNull() && list.getFieldData().isNotNull()) {
            var format = new PixbufFormat(new CPointer(list.getFieldData().getCPointer()));

            System.out.println("__");
            System.out.println("Format " + count + ":");
            System.out.println(format.getName());
            System.out.println(format.getDescription());
            if (format.isDisabled() == GTK.TRUE) {
                System.out.println("disabled");
            }

            if (format.isScalable() == GTK.TRUE) {
                System.out.println("scalable");
            }

            if (format.isWritable() == GTK.TRUE) {
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

    private int drawLogo(Context cr) {
        if (pixbuf != null) {
            cr.save();
            Gdk.cairoSetSourcePixbuf(cr, pixbuf, 0, 0);
            cr.paint();
            return GTK.TRUE;
        }
        return GTK.FALSE;
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
