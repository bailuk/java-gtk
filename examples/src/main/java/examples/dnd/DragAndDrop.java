package examples.dnd;

import ch.bailu.gtk.gdk.GdkConstants;
import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gio.File;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.ApplicationWindow;
import ch.bailu.gtk.gtk.DropTarget;
import ch.bailu.gtk.gtk.Label;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;

/**
 * https://docs.gtk.org/gtk4/drag-and-drop.html
 */
public class DragAndDrop {
    public final static Str ID = new Str("org.gtk.example.dnd");

    public static void main(String[] args) {

        var app = new Application(ID, ApplicationFlags.FLAGS_NONE);
        app.onActivate(() -> {
            // Get and initialize application window
            var window = new ApplicationWindow(app);
            window.setTitle("Drag and drop demo");
            window.setDefaultSize(400,300);

            // Create a label to display dropped files
            var label = new Label("Drop files here");
            label.setHexpand(true);
            label.setVexpand(true);

            // Crate drop target
            var target = new DropTarget(File.getTypeID(), GdkConstants.ACTION_ALL);
            target.onDrop((value, x, y) -> {
                System.out.println("dropped");
                var file = new File(value.getObject().cast());
                label.setText(file.getBasename());
                return true;
            });

            // Make the label a drop target
            label.addController(target);

            // Compose and display
            window.setChild(label);
            window.show();
        });


        System.exit(app.run(args.length, new Strs(args)));
    }
}
