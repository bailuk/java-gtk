package ch.bailu.gtk.helper;

import java.util.HashMap;

import ch.bailu.gtk.gio.Action;
import ch.bailu.gtk.gio.ActionMap;
import ch.bailu.gtk.gio.SimpleAction;
import ch.bailu.gtk.glib.Variant;
import ch.bailu.gtk.glib.VariantType;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;

/**
 * https://manpagez.com/html/glib/glib-2.40.0/gvariant-format-strings.php#gvariant-format-strings-numeric-types
 */
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

    public void add(String name, SimpleAction.OnActivate run) {
        var action = new SimpleAction(new Str(name), null);
        addAction(name, new Action(action.cast()));
        action.onActivate(run);
    }

    public void add(String name, boolean initial, SimpleAction.OnActivate run) {
        var action = SimpleAction.newStatefulSimpleAction(new Str(name), null, Variant.newBooleanVariant(initial));
        addAction(name, new Action(action.cast()));
        action.onActivate(parameter -> {
            toggleState(name);
            run.onActivate(parameter);
        });
    }

    public void add(String name, int initial, SimpleAction.OnActivate run) {
        var action = SimpleAction.newStatefulSimpleAction(new Str(name), new VariantType(new Str("i")), Variant.newInt32Variant(initial));

        addAction(name, new Action(action.cast()));
        action.onActivate(parameter -> {
            action.setState(parameter);
            run.onActivate(parameter);
        });
    }

    public void changeState(String name, boolean checked) {
        Action action = actions.get(name);

        if (action != null) {
            action.changeState(Variant.newBooleanVariant(checked));
        }
    }

    public boolean toggleState(String name) {
        Action action = actions.get(name);

        if (action != null) {
            action.changeState(Variant.newBooleanVariant(!action.getState().getBoolean()));
            return getBooleanState(name);
        }
        return false;
    }

    public int getState(String name) {
        Action action = actions.get(name);
        if (action != null) {
            return action.getState().getInt32();
        }
        return 0;
    }

    public boolean getBooleanState(String name) {
        Action action = actions.get(name);

        if (action != null) {
            return action.getState().getBoolean();
        }
        return false;
    }

    public void setAccels(String name, String[] accels) {
        if (accels != null && accels.length > 1 && accels[accels.length-1] == null) {
            app.setAccelsForAction(new Str(this.name + "." + name), new Strs(accels));
        }
    }

    private void addAction(String name, Action action) {
        map.addAction(action);
        actions.put(name, action);
    }
}
