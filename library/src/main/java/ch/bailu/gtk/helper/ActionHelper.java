package ch.bailu.gtk.helper;

import java.util.HashMap;

import ch.bailu.gtk.GTK;
import ch.bailu.gtk.gio.Action;
import ch.bailu.gtk.gio.ActionMap;
import ch.bailu.gtk.gio.SimpleAction;
import ch.bailu.gtk.glib.Variant;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;

public class ActionHelper {
    private final Application app;
    private final ActionMap map;
    private final String name;

    private final HashMap<String, Action> actions = new HashMap<>();

    public ActionHelper(Application app) {
        this.name = "app";
        this.app = app;
        this.map = new ActionMap(app.cast());
    }

    public ActionHelper(ActionMap map, String name) {
        this.map = map;
        this.name = name;
        this.app = null;
    }

    public void add(String name, SimpleAction.OnActivate run) {
        var action = new SimpleAction(new Str(name), null);
        addAction(name, new Action(action.cast()));
        action.onActivate(run);
    }

    private void addAction(String name, Action action) {
        map.addAction(action);
        actions.put(name, action);
    }

    public void addBoolean(String name, int initial, SimpleAction.OnActivate run) {
        var action = SimpleAction.newStatefulSimpleAction(new Str(name), null, Variant.newBooleanVariant(initial));
        addAction(name, new Action(action.cast()));
        action.onActivate(run);
    }

    public void toggle(String name) {
        Action action = actions.get(name);

        if (action != null) {
            action.changeState(Variant.newBooleanVariant(GTK.TOGGLE(action.getState().getBoolean())));
        }
    }

    public int getValue(String name) {
        Action action = actions.get(name);

        if (action != null) {
            return action.getState().getBoolean();
        }
        return GTK.FALSE;
    }

    public void setAccels(String name, String[] accels) {
        if (app != null && accels != null && accels.length > 1 && accels[accels.length-1] == null) {
            app.setAccelsForAction(new Str(this.name + "." + name), new Strs(accels));
        }
    }
}
