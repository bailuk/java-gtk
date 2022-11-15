package ch.bailu.gtk.lib.handler.action;

import ch.bailu.gtk.gio.Action;
import ch.bailu.gtk.gio.ActionMap;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gio.SimpleAction;
import ch.bailu.gtk.glib.Variant;
import ch.bailu.gtk.lib.handler.SignalHandler;
import ch.bailu.gtk.type.Strs;

/**
 * Manages application level ActionMap and offers Facade for Action Interface
 * Callbacks for integer and boolean types actions
 *
 */
public class ActionHandler {

    private final String name;
    private final Action action;
    private final SimpleAction simpleAction;

    private static ActionHandlerMap actionHandlerMap;

    public static void setAccels(Application app, String action, String... accels) {
        app.setAccelsForAction(action, Strs.nullTerminated(accels));
    }

    public void disconnectSignals() {

        action.disconnectSignals();
    }

    public interface OnActivate {
        void onActivate();
    }

    public interface OnActivateExtended {
        void onActivate(ActionHandler actionHandler, Variant variant);
    }

    public interface OnToggled {
        void onActivate(boolean value);
    }


    public interface OnChange {
        void onActivate(int state);
    }


    ActionHandler(String name, SimpleAction simpleAction) {
        this.name = name;
        this.simpleAction = simpleAction;
        this.action = new Action(simpleAction.cast());
    }

    public SignalHandler onActivate(SimpleAction.OnActivate signal) {
        return simpleAction.onActivate(signal);
    }

    public SignalHandler onActivate(OnActivate signal) {
        return simpleAction.onActivate((Variant variant) -> signal.onActivate());
    }

    public SignalHandler onActivate(OnActivateExtended signal) {
        return simpleAction.onActivate((Variant variant) -> signal.onActivate(this, variant));
    }

    public SignalHandler onToggle(OnToggled signal) {
        return simpleAction.onActivate((Variant variant) -> {
            toggle();
            signal.onActivate(getBoolean());
        });
    }

    public SignalHandler onChange(OnChange signal) {
        return simpleAction.onActivate((Variant variant) -> {
            changeState(variant);
            signal.onActivate(getInteger());
        });
    }


    /**
     * Toggle state of boolean state (checkbox) action
     */
    public void toggle() {
        changeBoolean(!getBoolean());
    }

    public void changeBoolean(boolean value) {
        action.changeState(Variant.newBooleanVariant(value));
    }

    public boolean getBoolean() {
        return action.getState().getBoolean();
    }

    public int getInteger() {
        return action.getState().getInt32();
    }

    public void changeInteger(int value) {
        action.changeState(Variant.newInt32Variant(value));
    }

    public void changeState(Variant variant) {
        action.changeState(variant);
    }

    public Variant getState() {
        return action.getState();
    }

    public static ActionHandler get(Application app, String name, boolean initial) {
        return map(app).get(name, initial);
    }

    public static ActionHandler get(Application app, String name, int initial) {
        return map(app).get(name, initial);
    }

    public static ActionHandler get(Application app, String name) {
        return map(app).get(name);
    }

    public void remove(Application app) {
        map(app).remove(name);
    }

    public void remove(Application app, String name) {
        map(app).remove(name);
    }

    private static ActionHandlerMap map(Application app) {
        if (actionHandlerMap == null) {
            actionHandlerMap = new ActionHandlerMap(new ActionMap(app.cast()));
        }
        return actionHandlerMap;
    }
}
