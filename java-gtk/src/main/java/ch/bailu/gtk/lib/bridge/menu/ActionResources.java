package ch.bailu.gtk.lib.bridge.menu;

import java.util.HashMap;

import ch.bailu.gtk.gio.Action;
import ch.bailu.gtk.gio.ActionMap;
import ch.bailu.gtk.gio.Application;
import ch.bailu.gtk.gio.SimpleAction;
import ch.bailu.gtk.lib.util.SizeLog;
import ch.bailu.gtk.type.Str;

public class ActionResources {

    private final ActionMap map;
    private final HashMap<String, Action> actions = new HashMap<>();

    public ActionResources(Application app) {
        this.map = new ActionMap(app.cast());
    }

    private final SizeLog sizeLog = new SizeLog(ActionResources.class.getSimpleName());


    public void add(String name, SimpleAction simpleAction) {
        var action = new Action(simpleAction.cast());

        map.addAction(action);
        actions.put(name, action);
        sizeLog.log(actions.size());
    }

    public void remove(String name) {
        var strName = new Str(name);
        map.removeAction(strName);
        actions.remove(name);
        strName.destroy();
    }

    Action get(String name) {
        return actions.get(name);
    }
}
