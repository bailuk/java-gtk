package examples.libadwaita.demo;

import ch.bailu.gtk.adw.Bin;
import ch.bailu.gtk.adw.MessageDialog;
import ch.bailu.gtk.adw.ResponseAppearance;
import ch.bailu.gtk.adw.Toast;
import ch.bailu.gtk.gobject.Gobject;
import ch.bailu.gtk.gobject.ObjectClassExtended;
import ch.bailu.gtk.gtk.WidgetClassExtended;
import ch.bailu.gtk.gtk.Window;
import ch.bailu.gtk.type.PointerContainer;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.gobject.TypeSystem;

public class AdwDemoPageDialogs extends Bin {
    private final static Str TYPE_NAME = new Str(AdwDemoPageDialogs.class.getSimpleName());
    private final static Str MESSAGE_DIALOG_ACTION = new Str("demo.message-dialog");

    private static long type = 0;
    private static int signal;

    public AdwDemoPageDialogs(PointerContainer self) {
        super(self);
    }

    public synchronized static long getTypeID() {
        if (type == 0) {
            type = TypeSystem.registerClass(Bin.getTypeID(), TYPE_NAME, 0, (__self, g_class, class_data) -> {
                var widgetClass = new WidgetClassExtended(g_class.cast());
                var objectClass = new ObjectClassExtended(g_class.cast());

                signal = objectClass.signalNew(AdwDemoPageToasts.ADD_TOAST_SIGNAL, TypeSystem.GTYPE_NONE , Toast.getTypeID());

                widgetClass.setTemplateOrExit("/adw_demo/adw-demo-page-dialogs.ui");

                widgetClass.installAction(
                        MESSAGE_DIALOG_ACTION,
                        null,
                        (__self1, widget, action_name, parameter) -> new AdwDemoPageDialogs(widget.cast()).onDemoMessageDialog()
                );

            }, (__self, instance, g_class) -> new Bin(instance.cast()).initTemplate());
        }
        return type;
    }

    private void onDemoMessageDialog() {
         var dialog = new MessageDialog(new Window(getRoot().cast()),
                 "Save Changes?",
                 "Open document contains unsaved changes. Changes which are not saved will be permanently lost.");

         dialog.addResponse("cancel", "_Cancel");
         dialog.addResponse("discard", "_Discard");
         dialog.addResponse("save", "_Save");

         dialog.setResponseAppearance("discard", ResponseAppearance.DESTRUCTIVE);
         dialog.setResponseAppearance("save", ResponseAppearance.SUGGESTED);

         dialog.setDefaultResponse("save");
         dialog.setCloseResponse("cancel");

         dialog.onResponse(response -> {
             var toast = new Toast("Dialog response: " + response);
             Gobject.signalEmit(AdwDemoPageDialogs.this, signal, 0, toast.asCPointer());
             dialog.disconnectSignals();
         });
         dialog.present();
    }
}
