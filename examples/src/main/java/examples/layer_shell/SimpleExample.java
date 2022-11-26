package examples.layer_shell;

import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.ApplicationWindow;
import ch.bailu.gtk.gtk.Label;
import ch.bailu.gtk.gtklayershell.Edge;
import ch.bailu.gtk.gtklayershell.Gtklayershell;
import ch.bailu.gtk.gtklayershell.Layer;
import ch.bailu.gtk.type.Strs;

/**
 * Port of
 * https://github.com/wmww/gtk-layer-shell/blob/gtk4/examples/simple-example.c
 * Layer shell for GTK4 seems to be work in progress.
 * So this probably does not (yet) work
 */
public class SimpleExample {

    public static void main(String[] args) {
        var app = new Application("sh.wmww.gtk-layer-example", ApplicationFlags.FLAGS_NONE);
        app.onActivate(() -> {
            var window = new ApplicationWindow(app);
            Gtklayershell.initForWindow(window);
            Gtklayershell.setLayer(window, Layer.BOTTOM);
            Gtklayershell.autoExclusiveZoneEnable(window);
            Gtklayershell.setMargin(window, Edge.LEFT, 40);
            Gtklayershell.setMargin(window, Edge.RIGHT, 40);
            Gtklayershell.setMargin(window, Edge.TOP, 40);

            boolean[] anchors = {true, true, false, true};
            for (int i = 0; i<anchors.length; i++) {
                Gtklayershell.setAnchor(window, i, anchors[i]);
            }

            // Set up a widget
            var label = new Label("");
            label.setMarkup ("<span font_desc=\"20.0\">GTK Layer Shell example!</span>");
            window.setChild(label);
            window.setMarginBottom(10);
            window.show();
        });

        app.run(args.length, new Strs(args));
    }
}
