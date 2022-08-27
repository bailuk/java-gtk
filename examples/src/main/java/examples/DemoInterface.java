package examples;

import ch.bailu.gtk.gtk.Window;
import ch.bailu.gtk.type.Str;

public interface DemoInterface {
    Window runDemo();
    Str getTitle();
    Str getDescription();
}
