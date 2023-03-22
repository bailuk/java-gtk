package examples.libadwaita.demo;

import ch.bailu.gtk.adw.AboutWindow;
import ch.bailu.gtk.adw.Bin;
import ch.bailu.gtk.gobject.TypeInstance;
import ch.bailu.gtk.gtk.License;
import ch.bailu.gtk.gtk.WidgetClassExtended;
import ch.bailu.gtk.gtk.Window;
import ch.bailu.gtk.type.CPointer;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;
import ch.bailu.gtk.type.gobject.TypeSystem;

public class AdwDemoPageAbout extends Bin {

    private static final String releaseNotes = "" +
            "<p>" +
            "This release adds the following features:" +
            "</p>" +
            "<ul>" +
            "    <li>Added a way to export fonts.</li>" +
            "    <li>Better support for <code>monospace</code> fonts.</li>" +
            "    <li>Added a way to preview <em>italic</em> text.</li>" +
            "    <li>Bug fixes and performance improvements.</li>" +
            "   <li>Translation updates.</li>" +
            "</ul>";

    private static final Strs developers = new Strs(new String[]{"Angela Avery <angela@example.org>", null});
    private static final Strs artists = new Strs(new String[]{"GNOME Design Team", null});
    private static final Strs specialThanks = new Strs(new String[]{"My cat", null});
    private final static Str TYPE_NAME = new Str(AdwDemoPageAbout.class.getSimpleName());
    private final static Str DEMO_ACTION = new Str("demo.run");

    private static long type = 0;


    public AdwDemoPageAbout(TypeInstance self) {
        super(self.cast());
        initTemplate();
    }

    public AdwDemoPageAbout(CPointer self) {
        super(self);
    }

    public void onDemoRun() {
        var root = getRoot();

        var about = new AboutWindow();

        about.setApplicationIcon("org.example.Typeset");
        about.setApplicationName("Typeset");
        about.setDeveloperName("Angela Avery");
        about.setVersion("1.2.3");
        about.setReleaseNotesVersion("1.2.0");
        about.setReleaseNotes(releaseNotes);
        about.setComments("Typeset is an app that doesn’t exist and is used as an example content for this about window.");
        about.setWebsite("https://example.org");
        about.setIssueUrl("https://example.org");
        about.setSupportUrl("https://example.org");
        about.setCopyright("© 2022 Angela Avery");
        about.setLicenseType(License.LGPL_2_1);
        about.setDevelopers(developers);
        about.setArtists(artists);
        about.addLink("Documentation", "https://gnome.pages.gitlab.gnome.org/libadwaita/doc/main/class.AboutWindow.html");
        about.addLegalSection("Fonts", null, License.CUSTOM, "This application uses font data from <a href='https://example.org'>somewhere</a>.");
        about.addAcknowledgementSection("Special thanks to", specialThanks);
        about.setTransientFor(new Window(root.cast()));
        about.present();
    }

    public synchronized static long getTypeID() {
        if (type == 0) {
            type = TypeSystem.registerClass(Bin.getTypeID(), TYPE_NAME, 0, (__self, g_class, class_data) -> {
                var widgetClass = new WidgetClassExtended(g_class.cast());
                widgetClass.setTemplateOrExit("/adw_demo/adw-demo-page-about.ui");
                widgetClass.installAction(DEMO_ACTION, Str.NULL, (__self1, widget, action_name, parameter) -> new AdwDemoPageAbout(widget.cast()).onDemoRun());
            }, (__self, instance, g_class) -> new AdwDemoPageAbout(instance));
        }
        return type;
    }
}
