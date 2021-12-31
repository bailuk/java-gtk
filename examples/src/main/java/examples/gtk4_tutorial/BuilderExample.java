package examples.gtk4_tutorial;

import ch.bailu.gtk.exception.AllocationError;
import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.Builder;
import ch.bailu.gtk.gtk.Button;
import ch.bailu.gtk.gtk.Window;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;

public class BuilderExample {
    private static final Str UI_XML = new Str(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<interface>\n" +
            "  <object id=\"window\" class=\"GtkWindow\">\n" +
            "    <property name=\"title\">Grid</property>\n" +
            "    <child>\n" +
            "      <object id=\"grid\" class=\"GtkGrid\">\n" +
            "        <child>\n" +
            "          <object id=\"button1\" class=\"GtkButton\">\n" +
            "            <property name=\"label\">Button 1</property>\n" +
            "            <layout>\n" +
            "              <property name=\"column\">0</property>\n" +
            "              <property name=\"row\">0</property>\n" +
            "            </layout>\n" +
            "          </object>\n" +
            "        </child>\n" +
            "        <child>\n" +
            "          <object id=\"button2\" class=\"GtkButton\">\n" +
            "            <property name=\"label\">Button 2</property>\n" +
            "            <layout>\n" +
            "              <property name=\"column\">1</property>\n" +
            "              <property name=\"row\">0</property>\n" +
            "            </layout>\n" +
            "          </object>\n" +
            "        </child>\n" +
            "        <child>\n" +
            "          <object id=\"quit\" class=\"GtkButton\">\n" +
            "            <property name=\"label\">Quit</property>\n" +
            "            <layout>\n" +
            "              <property name=\"column\">0</property>\n" +
            "              <property name=\"row\">1</property>\n" +
            "              <property name=\"column-span\">2</property>\n" +
            "            </layout>\n" +
            "          </object>\n" +
            "        </child>\n" +
            "      </object>\n" +
            "    </child>\n" +
            "  </object>\n" +
            "</interface>\n");


    public BuilderExample(String[] args) {
        final var app = new Application(new Str("org.gtk.example"), ApplicationFlags.FLAGS_NONE);
        app.onActivate(() -> {
            var builder = new Builder();
            try {
                builder.addFromString(UI_XML, UI_XML.getLength());

                var window = new Window(builder.getObject(new Str("window")).cast());
                window.setApplication(app);

                var button1 = new Button(builder.getObject(new Str("button1")).cast());
                button1.onClicked(()->System.out.println("Hello from button1"));

                var button2 = new Button(builder.getObject(new Str("button2")).cast());
                button2.onClicked(()->System.out.println("Hello from button2"));

                var quit = new Button(builder.getObject(new Str("quit")).cast());
                quit.onClicked(()->window.close());

                window.show();

            } catch (AllocationError e) {
                System.err.println(e.getMessage());
                app.quit();
            }
        });
        app.run(args.length, new Strs(args));
        app.unref();
    }
}
