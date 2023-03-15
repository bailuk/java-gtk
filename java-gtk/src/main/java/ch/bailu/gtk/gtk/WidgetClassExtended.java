package ch.bailu.gtk.gtk;

import com.sun.jna.Callback;

import java.io.IOException;

import ch.bailu.gtk.lib.util.JavaResource;
import ch.bailu.gtk.type.CPointer;
import ch.bailu.gtk.type.Str;

public class WidgetClassExtended extends WidgetClass {

    public WidgetClassExtended(CPointer g_class) {
        super(g_class);
    }

    public void bindTemplateCallback(String name, Callback cb) {
        JnaWidgetClass.INST().gtk_widget_class_bind_template_callback_full(getCPointer(), new Str(name).getCPointer(), cb);
    }

    public void setTemplateOrExit(String resource) {
        try {
            setTemplate(new JavaResource(resource).asBytes());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
