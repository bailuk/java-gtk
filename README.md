# Files
/usr/share/gir-1.0

/* this file is auto generated */


package ch.bailu.gtk.gtk;


```java
public class Application extends ch.bailu.gtk.gio.Application {

    public void onQueryEnd(ch.bailu.gtk.OnSignalInterface callback) { ch.bailu.gtk.Signal.connect(this, "query-end", callback); }
    public void onWindowAdded(ch.bailu.gtk.OnSignalInterface callback) { ch.bailu.gtk.Signal.connect(this, "window-added", callback); }
    public void onWindowRemoved() { ch.bailu.gtk.Signal.connect(this, "window-removed", callback); }

    public void onWindowRemoved(OnWindowRemoved signal) {
        signal.connect(getLong());
    }    
}

public abstract class OnWindowRemoved() {

    private final static Map<Long, List<OnWindowRemoved>> callbacks = new HashMap<>();

    public abstract void run(Window window); // <-- custom signature

    protected void connect(long caller) {
        callbacks.get(caller).add(this);

        signalConnect(caller);
    }

    private static void callback(long caller, long window) {  // <-- custom signature
        List<OnWindowRemvoed> observers = callbackMap.get(caller);
        for (OnWindowRemoved observer : observers) {
            observer.run(new Window(window));  // <-- custom signature
        }
    }

    private native void signalConnect(long caller);
}




    /* Unsupported:Parameter:[(s)void:void]:addAccelerator:[(s)const gchar*:String]:[(s)const gchar*:String]:[GVariant*:] */
    /* Unsupported:Return value:[gchar**:]:getAccelsForAction:[(s)const gchar*:String] */
    /* Unsupported:Return value:[gchar**:]:getActionsForAccel:[(s)const gchar*:String] */
    /* Unsupported:Return value:[GList*:]:getWindows */
    /* Unsupported:Return value:[gchar**:]:listActionDescriptions */
    /* Unsupported:Parameter:[(s)void:void]:removeAccelerator:[(s)const gchar*:String]:[GVariant*:] */
    /* Unsupported:Parameter:[(s)void:void]:setAccelsForAction:[(s)const gchar*:String]:[const gchar* const*:] */

}

```