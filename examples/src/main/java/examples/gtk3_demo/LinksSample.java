package examples.gtk3_demo;

import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.ApplicationWindow;
import ch.bailu.gtk.gtk.Label;
import ch.bailu.gtk.gtk.Window;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;

public class LinksSample {

    public LinksSample(String[] argv) {
        var app = new Application(new Str("com.example.GtkApplication"),
                ApplicationFlags.FLAGS_NONE);

        app.onActivate(() -> {

            // Create a new window
            var window = init(new ApplicationWindow(app));

            window.showAll();
            window.present();

        });

        app.run(argv.length, new Strs(argv));
    }




    private Window init(Window window) {
        StringBuilder labelString = new StringBuilder();

        labelString
                .append("Some <a href=\"http://en.wikipedia.org/wiki/Text\"")
                .append("title=\"plain text\">text</a> may be marked up\n")
                .append("as hyperlinks, which can be clicked\n")
                .append("or activated via <a href=\"keynav\">keynav</a>\n")
                .append("and they work fine with other markup, like when\n")
                .append("searching on <a href=\"http://www.google.com/\">")
                .append("<span color=\"#0266C8\">G</span><span color=\"#F90101\">o</span>")
                .append("<span color=\"#F2B50F\">o</span><span color=\"#0266C8\">g</span>")
                .append("<span color=\"#00933B\">l</span><span color=\"#F90101\">e</span>")
                .append("</a>.");
        window.setTitle(new Str("Links"));
        window.setBorderWidth(12);
        Label label = new Label(new Str(labelString.toString()));

        label.setUseMarkup(1);
        label.onActivateLink(uri -> {
            System.out.println("link clicked");
            return 0;
        });
        window.add(label);
        label.show();
        return window;
    }

}
