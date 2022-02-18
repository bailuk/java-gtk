package examples;

import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.ApplicationWindow;
import ch.bailu.gtk.gtk.CustomLayout;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;

public class CustomLayoutTest {
    public CustomLayoutTest(String[] args) {
        var app = new Application(new Str("com.example.GtkApplication"),
                ApplicationFlags.FLAGS_NONE);

        app.onActivate(() -> {
            // Create a new window
            var window = new ApplicationWindow(app);

            var layoutManager = new CustomLayout(widget -> {
                System.out.println("onCustomRequestModeFunc called");
                return 0;
            }, (widget, orientation, for_size, minimum, natural, minimum_baseline, natural_baseline) -> {
                System.out.println("onCustomMeasureFunc called");
                System.out.println(orientation + ":" + for_size);
            }, (widget, width, height, baseline) -> {
                System.out.println("onCustomAllocateFunc called");
                System.out.println(width + ":" + height + ":" + baseline);
            });
            window.setLayoutManager(layoutManager);

            window.setDefaultSize(100,100);
            window.show();
        });

        app.run(args.length, new Strs(args));
    }
}
