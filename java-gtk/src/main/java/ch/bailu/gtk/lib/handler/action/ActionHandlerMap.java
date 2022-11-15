package ch.bailu.gtk.lib.handler.action;

import java.io.PrintStream;
import java.util.HashMap;

import ch.bailu.gtk.gio.Action;
import ch.bailu.gtk.gio.ActionMap;
import ch.bailu.gtk.gio.SimpleAction;
import ch.bailu.gtk.glib.Variant;
import ch.bailu.gtk.glib.VariantType;
import ch.bailu.gtk.lib.util.SizeLog;
import ch.bailu.gtk.type.Str;

public class ActionHandlerMap {
    private final static Str INTEGER = new Str("i");


    private final ActionMap map;
    private final HashMap<String, ActionHandler> handlerMap = new HashMap<>();

    ActionHandlerMap(ActionMap map) {
        this.map = map;
    }

    private final SizeLog sizeLog = new SizeLog(ActionHandler.class.getSimpleName());

    private interface Factory {
        SimpleAction create(Str name);
    }

    public ActionHandler get(String name, int initial) {
        return get(name, (strName) -> SimpleAction.newStatefulSimpleAction(strName, new VariantType(INTEGER), Variant.newInt32Variant(initial)));
    }

    public ActionHandler get(String name, boolean initial) {
        return get(name, (strName) -> SimpleAction.newStatefulSimpleAction(strName, null, Variant.newBooleanVariant(initial)));
    }

    public ActionHandler get(String name) {
        return get(name, (strName) -> new SimpleAction(strName, null));
    }


    private ActionHandler get(String name, Factory factory) {
        var strName = new Str(name);
        removeIfNotInMap(name, strName);
        return createIfNotExists(name, strName, factory);

    }


    public void remove(String name) {
        var actionHandler = handlerMap.remove(name);
        if (actionHandler != null) {
            actionHandler.disconnectSignals();
        }

        var strName = new Str(name);
        map.removeAction(strName);
        strName.destroy();
    }

    private void removeIfNotInMap(String name, Str strName) {
        if (handlerMap.containsKey(name)) {
            if (map.lookupAction(strName).isNull()) {
                remove(name);
            }
        }
    }

    private ActionHandler createIfNotExists(String name, Str strName, Factory factory) {
        if (!handlerMap.containsKey(name)) {
            var simpleAction = new SimpleAction(map.lookupAction(strName).cast());

            if (simpleAction.isNull()) {
                simpleAction = factory.create(strName);
                map.addAction(new Action(simpleAction.cast()));
            }

            handlerMap.put(name, new ActionHandler(name, simpleAction));
            sizeLog.log(handlerMap.size());
        }
        return handlerMap.get(name);
    }

    public void dump(PrintStream out) {
        out.println("_");
        out.println(ActionHandler.class.getSimpleName());
        out.println("=".repeat(ActionHandler.class.getSimpleName().length()));
        out.println(handlerMap.size());
        handlerMap.keySet().forEach(out::println);
    }

}
