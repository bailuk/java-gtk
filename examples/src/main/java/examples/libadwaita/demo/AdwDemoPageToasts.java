package examples.libadwaita.demo;

import com.sun.jna.Structure;

import ch.bailu.gtk.adw.Bin;
import ch.bailu.gtk.adw.Toast;
import ch.bailu.gtk.gobject.Gobject;
import ch.bailu.gtk.gobject.ObjectClassExtended;
import ch.bailu.gtk.gobject.TypeInstance;
import ch.bailu.gtk.gtk.WidgetClassExtended;
import ch.bailu.gtk.type.CPointer;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.gobject.TypeSystem;

public class AdwDemoPageToasts extends Bin {
    public static Str ADD_TOAST_SIGNAL = new Str("add-toast");
    private final static Str TYPE_NAME = new Str(AdwDemoPageToasts.class.getSimpleName());

    private final static long PARENT_TYPE = Bin.getTypeID();
    private final static int PARENT_INSTANCE_SIZE = TypeSystem.getTypeSize(PARENT_TYPE).instanceSize;

    private static long type = 0;
    private static int signal;

    private static final Str TOAST_UNDO_ACTION_NAME = new Str("toast.undo");
    private static final Str TOAST_DISMISS_ACTION_NAME = new Str("toast.dismiss");

    public AdwDemoPageToasts(CPointer self) {
        super(self);
        instance = new Instance(getCPointer());
    }

    public AdwDemoPageToasts(TypeInstance instance) {
        super(instance.cast());
        initTemplate();
        this.instance = new Instance(getCPointer());
        actionSetEnabled(TOAST_DISMISS_ACTION_NAME, false);
    }

    @Structure.FieldOrder({"parent", "undo_toast", "toast_undo_items"})
    public static class Instance extends Structure {
        public Instance(long _self) {
            super(toJnaPointer(_self));
            read();
        }

        public byte[] parent = new byte[PARENT_INSTANCE_SIZE];
        public long undo_toast;
        public int toast_undo_items;
    }

    private final Instance instance;

    public synchronized static long getTypeID() {
        if (type == 0) {
            type = TypeSystem.registerClass(PARENT_TYPE, TYPE_NAME, 8+4, (__self, g_class, class_data) -> {
                var widgetClass = new WidgetClassExtended(g_class.cast());
                var objectClass = new ObjectClassExtended(g_class.cast());

                signal = objectClass.signalNew(AdwDemoPageToasts.ADD_TOAST_SIGNAL, TypeSystem.GTYPE_NONE , Toast.getTypeID());

                widgetClass.setTemplateOrExit("/adw_demo/adw-demo-page-toasts.ui");

                widgetClass.installAction("toast.add", null, (__self1, widget, action_name, parameter) -> new AdwDemoPageToasts(widget.cast()).onAddToast());
                widgetClass.installAction("toast.add-with-button", null, (__self12, widget, action_name, parameter) -> new AdwDemoPageToasts(widget.cast()).onAddWithButton());
                widgetClass.installAction("toast.add-with-long-title", null, (__self13, widget, action_name, parameter) -> new AdwDemoPageToasts(widget.cast()).onAddToastWithLongTitle());
                widgetClass.installAction("toast.dismiss", null, (__self14, widget, action_name, parameter) -> new AdwDemoPageToasts(widget.cast()).onToastDismiss());

            }, (__self, instance, g_class) -> new AdwDemoPageToasts(instance));
        }
        return type;
    }


    public void toastsUndo() {
        var title = new Str("Undoing deleting <span font_features='tnum=1'>" + instance.toast_undo_items + "</span> items…");
        var toast = new Toast(title);
        // toast.setProperty(ToastPriority.HIGH);
        // adw_toast_set_priority (toast, ADW_TOAST_PRIORITY_HIGH);
        addToast(toast);
        title.destroy();
    }

    public void addToast (Toast toast) {
        Gobject.signalEmit(this, signal, 0, toast.getCPointer());
    }

    private void onDismissed() {
        instance.undo_toast = 0L;
        instance.toast_undo_items = 0;

        actionSetEnabled(TOAST_DISMISS_ACTION_NAME, false);
    }

    private void onAddToast() {
        addToast(new Toast("Simple Toast"));
    }

    private void onAddWithButton() {
        instance.toast_undo_items++;

        var toast = new Toast(toCPointer(instance.undo_toast));

        if (toast.isNotNull()) {
            var title = new Str("<span font_features='tnum=1'>"+ instance.toast_undo_items +"</span> items deleted");
            toast.setTitle(title);
            addToast(toast);
            title.destroy();
        } else {
            var newToast = new Toast("blah");
            instance.undo_toast = newToast.getCPointer();

            toast.setButtonLabel("Undo");
            toast.setActionName(TOAST_UNDO_ACTION_NAME);
            toast.onDismissed(this::onDismissed);
            addToast(toast);
            actionSetEnabled(TOAST_DISMISS_ACTION_NAME, true);
        }

        instance.write();
    }

    private void onAddToastWithLongTitle() {
        var toast = new Toast("Lorem ipsum dolor sit amet, " +
                "consectetur adipiscing elit, " +
                "sed do eiusmod tempor incididunt " +
                "ut labore et dolore magnam aliquam " +
                "quaerat voluptatem.");

        addToast(toast);
    }

    private void onToastDismiss() {
        var toast = new Toast(toCPointer(instance.undo_toast));
        if (toast.isNotNull()) {
            toast.dismiss();
        }
    }
}
