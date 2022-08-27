package examples.gtk4_demo;

import ch.bailu.gtk.gtk.Box;
import ch.bailu.gtk.gtk.Button;
import ch.bailu.gtk.gtk.Label;
import ch.bailu.gtk.gtk.Orientation;
import ch.bailu.gtk.gtk.Window;
import ch.bailu.gtk.type.Str;
import examples.DemoInterface;

public class HelloWorldBoxed implements DemoInterface {
    private static final Str TITLE = new Str("Hello world boxed");

    private static final int MARGIN = 30;

    @Override
    public Window runDemo() {
        Window window = new Window();

        var verticalBox = new Box(Orientation.VERTICAL, 0);
        var buttonBox = new Box(Orientation.HORIZONTAL, 0);
        buttonBox.addCssClass(new Str("linked"));
        buttonBox.setMarginBottom(MARGIN);
        buttonBox.setMarginTop(MARGIN);
        buttonBox.setMarginStart(MARGIN);
        buttonBox.setMarginEnd(MARGIN);
        verticalBox.append(buttonBox);

        var label = new Label(Str.NULL);
        verticalBox.append(label);
        verticalBox.setMarginBottom(MARGIN);

        var buttonHello = new Button();
        buttonHello.setLabel(new Str("Hello"));
        buttonHello.onClicked(() -> label.setText(new Str("Hello")));
        buttonBox.append(buttonHello);

        var buttonWorld = new Button();
        buttonWorld.setLabel(new Str("World"));
        buttonWorld.onClicked(() -> label.setMarkup(new Str("<b>World</b>")));
        buttonBox.append(buttonWorld);

        window.setChild(verticalBox);
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
