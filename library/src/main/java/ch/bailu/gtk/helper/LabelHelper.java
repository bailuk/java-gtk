package ch.bailu.gtk.helper;

import ch.bailu.gtk.gtk.Label;
import ch.bailu.gtk.type.Str;

public class LabelHelper {
    public static void setLabel(Label label, String text) {
        Str old = label.getLabel();
        label.setLabel(new Str(text));
        if (old.isNotNull()) old.destroy();
    }
}
