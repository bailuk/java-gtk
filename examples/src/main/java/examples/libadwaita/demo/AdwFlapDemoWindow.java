package examples.libadwaita.demo;

import com.sun.jna.Callback;
import com.sun.jna.Structure;

import ch.bailu.gtk.adw.EnumListItem;
import ch.bailu.gtk.adw.Flap;
import ch.bailu.gtk.adw.FlapFoldPolicy;
import ch.bailu.gtk.adw.FlapTransitionType;
import ch.bailu.gtk.adw.Window;
import ch.bailu.gtk.gtk.PackType;
import ch.bailu.gtk.gtk.ToggleButton;
import ch.bailu.gtk.gtk.Widget;
import ch.bailu.gtk.gtk.WidgetClassExtended;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.gobject.TypeSystem;

public class AdwFlapDemoWindow extends Window {
    private final static Str TYPE_NAME = new Str(AdwFlapDemoWindow.class.getSimpleName());
    private final static int OFFSET = TypeSystem.getTypeSize(Window.getTypeID()).instanceSize;
    private static long type = 0;

    @Structure.FieldOrder({"parent", "flap", "reveal_btn_start", "reveal_btn_end"})
    public static class Instance extends Structure {
        public Instance(long _self) {
            super(toJnaPointer(_self));
            read();
        }

        public byte[] parent = new byte[OFFSET];
        public long flap;
        public long reveal_btn_start;
        public long reveal_btn_end;
    }

    private final Instance instance;

    public AdwFlapDemoWindow(long self) {
        super(toCPointer(self));
        instance = new Instance(self);
    }

    public AdwFlapDemoWindow() {
        super(TypeSystem.newInstance(getTypeID()));
        instance = new Instance(getCPointer());
    }

    public static Str foldPolicyName(EnumListItem item) {
        switch (item.getValue()) {
            case FlapFoldPolicy.ALWAYS:
                return new Str("Always");
            case FlapFoldPolicy.NEVER:
                return new Str("Never");
            case FlapFoldPolicy.AUTO:
                return new Str("Auto");
        }
        return Str.NULL;
    }

    public static Str transitionTypeName(EnumListItem item) {
        switch (item.getValue()) {
            case FlapTransitionType.OVER:
                return new Str("Over");
            case FlapTransitionType.SLIDE:
                return new Str("Slide");
            case FlapTransitionType.UNDER:
                return new Str("Under");
        }
        return Str.NULL;
    }

    public void onButtonToggled(ToggleButton button) {
        var flap = new Flap(toCPointer(instance.flap));
        var btnEnd = new Widget(toCPointer(instance.reveal_btn_end));
        var btnStart = new Widget(toCPointer(instance.reveal_btn_start));

        if (button.getActive()) {
            flap.setFlapPosition(PackType.START);
            btnEnd.setVisible(false);
            btnStart.setVisible(true);
        } else {
            flap.setFlapPosition(PackType.END);
            btnEnd.setVisible(true);
            btnStart.setVisible(false);
        }
    }

    public void onStackNotifyVisibleChild() {
        var flap = new Flap(toCPointer(instance.flap));
        if (flap.getFolded()&& !flap.getLocked()) {
            flap.setRevealFlap(false);
        }
    }

    public synchronized static long getTypeID() {
        if (type == 0) {
            type = TypeSystem.registerClass(Window.getTypeID(), TYPE_NAME, 3*8, (__self, g_class, class_data) -> {
                var widgetClass = new WidgetClassExtended(g_class.cast());
                widgetClass.setTemplateOrExit("/adw_demo/adw-flap-demo-window.ui");

                widgetClass.bindTemplateChildFull("flap", true, OFFSET);
                widgetClass.bindTemplateChildFull("reveal_btn_start", true, OFFSET + 8);
                widgetClass.bindTemplateChildFull("reveal_btn_end", true, OFFSET + 16);

                widgetClass.bindTemplateCallback("start_toggle_button_toggled_cb", new Callback() {
                    public void invoke(long button, long self) {
                        new AdwFlapDemoWindow(self).onButtonToggled(new ToggleButton(toCPointer(button)));
                    }
                });
                widgetClass.bindTemplateCallback("stack_notify_visible_child_cb", new Callback() {
                    public void invoke(long self) {
                        new AdwFlapDemoWindow(self).onStackNotifyVisibleChild();
                    }
                });
                widgetClass.bindTemplateCallback("fold_policy_name", new Callback() {
                    public long invoke(long item, long data) {
                        return foldPolicyName(new EnumListItem(toCPointer(item))).getCPointer();
                    }
                });
                widgetClass.bindTemplateCallback("transition_type_name", new Callback() {
                    public long invoke(long item, long data) {
                        return transitionTypeName(new EnumListItem(toCPointer(item))).getCPointer();
                    }
                });
            }, (__self, instance, g_class) -> new Window(instance.cast()).initTemplate());
        }
        return type;
    }
}
