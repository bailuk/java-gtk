package examples;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import ch.bailu.gtk.GTK;
import ch.bailu.gtk.bridge.Image;
import ch.bailu.gtk.cairo.Context;
import ch.bailu.gtk.gdk.Gdk;
import ch.bailu.gtk.gdkpixbuf.Pixbuf;
import ch.bailu.gtk.gdkpixbuf.PixbufFormat;
import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.ApplicationWindow;
import ch.bailu.gtk.gtk.DrawingArea;
import ch.bailu.gtk.type.CPointer;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;

public class ImageBridge {

    public ImageBridge(String[] argv) {

        listSupportedFormats();


        var app = new Application(new Str("org.gtk.example"), ApplicationFlags.FLAGS_NONE);
        app.onActivate(() -> doLogoLoadAndDisplay(new ApplicationWindow(app)));
        app.run(argv.length, new Strs(argv));

    }

    private void listSupportedFormats() {
        var list = Pixbuf.getFormats();

        int count = 1;
        while(list.isNotNull() && list.getFieldData().isNotNull()) {
            var format = new PixbufFormat(new CPointer(list.getFieldData().getCPointer()));

            System.out.println("");
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

    private Pixbuf pixbuf = null;

    private void doLogoLoadAndDisplay(ApplicationWindow window) {
        Pixbuf icon = loadPixbuf(64, 64);

        window.setResizable(GTK.TRUE);
        window.setSizeRequest(400,200);
        window.setTitle(new Str("GTK Logo from Java stream"));
        if (icon != null) {
            window.setIcon(icon);
        }

        window.onSizeAllocate(allocation -> setPixbuf(allocation.getFieldWidth(), allocation.getFieldHeight()));
        DrawingArea da = new DrawingArea();
        window.add(da);
        da.onDraw(cr -> drawLogo(cr));
        window.showAll();
    }


    private void setPixbuf(int width, int height) {
        Pixbuf pixbufNew = loadPixbuf(width, height);
        if (pixbufNew != null) {
            if (pixbuf != null) {
                pixbuf.unref();
            }
            pixbuf = pixbufNew;
        }
    }


    private Pixbuf loadPixbuf(int width, int height) {
        Pixbuf result = null;

        FileInputStream inputStream = null;
        try {
            File file = new File("src/main/resources/GTK.svg");
            inputStream = new FileInputStream(file);

            result = Image.load(inputStream, width, height);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR reading image");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    System.out.println("ERROR closing stream");
                }
            }
        }
        return result;
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
}
