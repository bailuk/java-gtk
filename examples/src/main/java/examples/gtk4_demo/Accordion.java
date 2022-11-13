package examples.gtk4_demo;

import ch.bailu.gtk.gtk.Align;
import ch.bailu.gtk.gtk.Box;
import ch.bailu.gtk.gtk.Button;
import ch.bailu.gtk.gtk.Orientation;
import ch.bailu.gtk.gtk.Window;
import ch.bailu.gtk.lib.bridge.CSS;
import ch.bailu.gtk.type.Str;
import examples.DemoInterface;

public class Accordion implements DemoInterface {
    private static Str name = new Str("CSS Accordion");

    @Override
    public Window runDemo() {
        var window = new Window();
        window.setDefaultSize(600, 300);

        var container = new Box(Orientation.HORIZONTAL, 0);
        container.setHalign(Align.CENTER);
        container.setValign(Align.CENTER);

        container.append(Button.newWithLabelButton("This"));
        container.append(Button.newWithLabelButton("Is"));
        container.append(Button.newWithLabelButton("A"));
        container.append(Button.newWithLabelButton("CSS"));
        container.append(Button.newWithLabelButton("Accordion"));
        container.append(Button.newWithLabelButton(":-)"));

        window.setChild(container);

        try {
            CSS.addProviderRecursive(window, "/css_accordion/css_accordion.css");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        window.show();
        return window;
    }

    @Override
    public Str getTitle() {
        return name;
    }

    @Override
    public Str getDescription() {
        return name;
    }
}
