package ch.bailu.gtk.gtk;

import com.sun.jna.Callback;

import java.io.IOException;

import ch.bailu.gtk.lib.util.JavaResource;
import ch.bailu.gtk.type.PointerContainer;
import ch.bailu.gtk.type.Str;

public class WidgetClassExtended extends WidgetClass {

    public WidgetClassExtended(PointerContainer g_class) {
        super(g_class);
    }

    public void bindTemplateCallback(String name, Callback cb) {
        JnaWidgetClass.INST().gtk_widget_class_bind_template_callback_full(asCPointer(), new Str(name).asCPointer(), cb);
    }

    public void setTemplateOrExit(String resource) {
        try {
            setTemplate(new JavaResource(resource).asBytes());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    @FunctionalInterface
    public interface SizeAllocateCallback extends com.sun.jna.Callback {
        void invoke(long widget, int widht, int height, int baseline);
    }

    public void overrideSizeAllocate(SizeAllocateCallback cp) {
        System.out.println("TODO implement overrideSizeAllocate");
    }

}
