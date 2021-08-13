package ch.bailu.gtk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Signal {

    private static Map<String, OnSignalInterface> callbacks = new HashMap<>();


    public static void connect(Pointer pointer, String signal, OnSignalInterface callback) {
        System.out.println("Java connect: " + signal);
        callbacks.put(signal, callback);
        doConnect(pointer.toLong(), signal);
    }

//g_signal_connect (app, "activate", G_CALLBACK (on_activate), NULL);
    public static native void doConnect(long pointer, String signal);


    public static void callback(String signal) {
        System.out.println("Java received: " + signal);

        OnSignalInterface callback = callbacks.get(signal);
        if (callback != null) {
            System.out.println("Java call: " + signal);
            callback.run();
        }
    }


    private final static MMap<Long, String, List<Object>> signalObservers = new MMap<>();

    public static void put(long emitter, String key2, Object observer) {
        System.out.println(key2);
        if (!signalObservers.containsKey(emitter, key2)) {
            signalObservers.put(emitter, key2, new ArrayList<>());
        }
        signalObservers.get(emitter, key2).add(observer);
    }

    public static List<Object> get(long emitter, String key2) {
        System.out.println(key2);
        return signalObservers.get(emitter, key2);
    }
}
