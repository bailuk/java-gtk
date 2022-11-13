package ch.bailu.gtk.lib.bridge.menu;

import java.util.HashMap;

import ch.bailu.gtk.gio.Action;
import ch.bailu.gtk.gio.ActionMap;
import ch.bailu.gtk.gio.Application;
import ch.bailu.gtk.gio.SimpleAction;
import ch.bailu.gtk.type.Str;

public class ActionResources {

    private final ActionMap map;
    private final HashMap<String, Action> actions = new HashMap<>();

    public ActionResources(Application app) {
        this.map = new ActionMap(app.cast());
    }

    private Long lastLog = System.currentTimeMillis();
    private int lastSize = 0;


    public void add(String name, SimpleAction simpleAction) {
        var action = new Action(simpleAction.cast());

        map.addAction(action);
        actions.put(name, action);

        log();
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


    private void log() {
        var now = System.currentTimeMillis();
        if (now - lastLog > 5000) {
            var size = actions.size();
            if (lastSize != size) {
                System.out.println("REFS (Action): " + size);
                lastLog = now;
                lastSize = size;
            }
        }
    }
}
