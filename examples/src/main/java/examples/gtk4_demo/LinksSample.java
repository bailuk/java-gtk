package examples.gtk4_demo;

import ch.bailu.gtk.gtk.Label;
import ch.bailu.gtk.gtk.Window;
import ch.bailu.gtk.type.Str;
import examples.DemoInterface;

public class LinksSample implements DemoInterface {

    private static final Str TITLE = new Str("Links");
    private static final Str LABEL = new Str(
            "Some <a href=\"http://en.wikipedia.org/wiki/Text\"" +
            "title=\"plain text\">text</a> may be marked up\n" +
            "as hyperlinks, which can be clicked\n" +
            "or activated via <a href=\"keynav\">keynav</a>\n" +
            "and they work fine with other markup, like when\n" +
            "searching on <a href=\"http://www.google.com/\">" +
            "<span color=\"#0266C8\">G</span><span color=\"#F90101\">o</span>" +
            "<span color=\"#F2B50F\">o</span><span color=\"#0266C8\">g</span>" +
            "<span color=\"#00933B\">l</span><span color=\"#F90101\">e</span>" +
            "</a>."
    );

    @Override
    public Window runDemo() {
        var window  = new Window();
        Label label = new Label(LABEL);

        label.setUseMarkup(1);
        label.onActivateLink(uri -> {
            System.out.println("link clicked");
            return 0;
        });
        window.setChild(label);
        label.show();
        return window;
    }

    @Override
    public Str getTitle() {
        return TITLE;
    }

    @Override
    public Str getDescription() {
        return TITLE;
    }
}
