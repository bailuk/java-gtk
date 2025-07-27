package examples.image_io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ch.bailu.gtk.cairo.Cairo;
import ch.bailu.gtk.cairo.Format;
import ch.bailu.gtk.cairo.Surface;
import ch.bailu.gtk.gdk.Gdk;
import ch.bailu.gtk.gdkpixbuf.Pixbuf;
import ch.bailu.gtk.gio.File;
import ch.bailu.gtk.gtk.Box;
import ch.bailu.gtk.gtk.Button;
import ch.bailu.gtk.gtk.DrawingArea;
import ch.bailu.gtk.gtk.DropDown;
import ch.bailu.gtk.gtk.FileDialog;
import ch.bailu.gtk.gtk.Label;
import ch.bailu.gtk.gtk.Orientation;
import ch.bailu.gtk.gtk.Widget;
import ch.bailu.gtk.gtk.Window;
import ch.bailu.gtk.lib.bridge.Image;
import ch.bailu.gtk.lib.util.JavaResource;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;
import ch.bailu.gtk.type.exception.AllocationError;
import examples.DemoInterface;

public class ImageIo implements DemoInterface {
    private static int SIZE = 500;
    private static Str TITLE = new Str("Image IO");
    private static Str DESCRIPTION = new Str("Load and save images using different methods");

    private DrawingArea drawingArea;
    private Surface imageSurface;
    private Label statusLabel;
    private DropDown methodDropdown;

    @Override
    public Window runDemo() {
        statusLabel = new Label(TITLE);
        statusLabel.setXalign(0);

        var vbox = new Box(Orientation.VERTICAL, 5);
        var hbox = new Box(Orientation.HORIZONTAL, 5);
        var demoWindow = new Window();

        hbox.append(createOpenButton(demoWindow));
        hbox.append(createSaveButton(demoWindow));
        hbox.append(createLoadDefaultButton());
        hbox.append(new Label("Method:"));
        hbox.append(createMethodDropDown());

        vbox.append(hbox);
        vbox.append(createDrawingArea());
        vbox.append(statusLabel);
        vbox.setMarginTop(5);
        vbox.setMarginBottom(5);
        vbox.setMarginStart(5);
        vbox.setMarginEnd(5);

        loadDefaultImage();

        demoWindow.setSizeRequest(SIZE, SIZE);
        demoWindow.setChild(vbox);
        return demoWindow;
    }

    private Widget createDrawingArea() {
        drawingArea = new DrawingArea();
        drawingArea.setHexpand(true);
        drawingArea.setVexpand(true);

        drawingArea.setDrawFunc((__self, drawing_area, cr, width, height, user_data) -> {
            if (imageSurface != null) {
                cr.save();
                cr.setSourceSurface(imageSurface, 0, 0);
                cr.paint();
            }
        }, null, null);
        return drawingArea;
    }

    private Widget createMethodDropDown() {
        var strs = new Strs(new String[]{"Cairo PNG", "Image Bridge"});
        methodDropdown = DropDown.newFromStringsDropDown(strs);
        return methodDropdown;
    }

    private Widget createLoadDefaultButton() {
        var result = new Button();
        result.setLabel("GTK.svg");
        result.onClicked(()-> loadDefaultImage());
        return result;
    }

    private Widget createSaveButton(Window parent) {
        var result = new Button();
        result.setLabel("Save…");
        result.onClicked(()->{
            var fileDialog = new FileDialog();
            fileDialog.save(parent, null, (__self, source_object, res, data) -> {
                try {
                    saveImage(fileDialog.saveFinish(res));
                } catch (AllocationError e) {
                    statusLabel.setLabel(e.getMessage());
                }
            }, null);
        });
        return result;
    }

    private Widget createOpenButton(Window parent) {
        var result = new Button();
        result.setLabel("Open…");
        result.onClicked(()->{
            var fileDialog = new FileDialog();
            fileDialog.open(parent, null, (__self, source_object, res, data) -> {
                try {
                    loadImage(fileDialog.openFinish(res));
                } catch (AllocationError e) {
                    statusLabel.setLabel(e.getMessage());
                }
            }, null);
        });
        return result;
    }

    private void loadDefaultImage() {
        var path = "/GTK.svg";

        try (InputStream inputStream = new JavaResource(path).asStream()) {
            var start = System.currentTimeMillis();

            var pixbuf =  Image.load(inputStream, SIZE, SIZE);
            var surface = Cairo.imageSurfaceCreate(Format.ARGB32, SIZE, SIZE);
            var context = surface.createContext();
            Gdk.cairoSetSourcePixbuf(context,pixbuf,0,0);
                context.paint();
                pixbuf.unref();
                var delta = System.currentTimeMillis() - start;

            statusLabel.setLabel(delta + "ms: R " + path);
            imageSurface = surface;
            drawingArea.queueDraw();
        } catch (IOException e) {
            statusLabel.setLabel(e.getMessage());
        }
    }

    private void saveImage(File file) {
        if (file != null && file.isNotNull()) {
            var path = file.getPath();
            var start = System.currentTimeMillis();

            if (methodDropdown.getSelected() == 0) {
                imageSurface.writeToPng(path);
            } else {
                imageBridgeWrite(path.toString());
            }

            var delta = System.currentTimeMillis() - start;
            statusLabel.setLabel(delta + "ms: W " + path);
            path.destroy();
        }
    }

    private void imageBridgeWrite(String path) {
        try (OutputStream outputStream = new FileOutputStream(path)){
            Pixbuf pixbuf = Gdk.pixbufGetFromSurface(imageSurface, 0, 0, imageSurface.getWidth(), imageSurface.getHeight());
            Image.save(outputStream, pixbuf, "png");
            pixbuf.unref();
        } catch (IOException e) {
            statusLabel.setText(e.getMessage());
        }
    }

    private void loadImage(File file) {
        if (file != null && file.isNotNull()) {
            var path = file.getPath();

            var start = System.currentTimeMillis();
            Surface surface;

            if (methodDropdown.getSelected() == 0) {
                surface = Cairo.imageSurfaceCreateFromPng(path);
            } else {
                surface = imageBridgeRead(path.toString());
            }

            if (surface != null) {
                var delta = System.currentTimeMillis() - start;
                statusLabel.setLabel(delta + "ms: R " + path);
                imageSurface = surface;
                drawingArea.queueDraw();
            }
            path.destroy();
        }
    }

    private Surface imageBridgeRead(String path) {
        try (InputStream inputStream = new FileInputStream(path)) {
            var pixbuf =  Image.load(inputStream);
            var surface = Cairo.imageSurfaceCreate(Format.ARGB32, pixbuf.getWidth(), pixbuf.getHeight());
            var context = surface.createContext();
            Gdk.cairoSetSourcePixbuf(context,pixbuf,0,0);
            context.paint();
            pixbuf.unref();
            return surface;
        } catch (IOException e) {
            statusLabel.setText(e.getMessage());
        }
        return null;
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
