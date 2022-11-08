package ch.bailu.gtk.lib.callback;

import java.util.Objects;

import ch.bailu.gtk.lib.util.MMap;
import ch.bailu.gtk.lib.util.SizeLog;
import ch.bailu.gtk.type.Pointer;

public class Callback {
    private final Pointer instance;
    private final String detailedCallback;
    private com.sun.jna.Callback callback;
    private final long callbackId = CallbackID.gen();

    private static final MMap<Long, Long, Callback> mmap = new MMap<>();
    private static final SizeLog sizeLog = new SizeLog(Callback.class.getSimpleName());

    public Callback(Pointer instance, String detailedCallback) {
        this.instance = instance;
        this.detailedCallback = detailedCallback;
    }

    public synchronized void register(com.sun.jna.Callback callback)  {
        if (this.callback == null) {
            this.callback = callback;
            mmap.put(instance.getCPointer(), callbackId, this);
            sizeLog.log(mmap.size());
        }
    }

    public synchronized void unregister() {
        if (this.callback != null) {
            mmap.remove(instance.getCPointer(), callbackId);
            this.callback = null;
        }
    }

    public static void unregister(Pointer instance) {
        synchronized (mmap) {
            var values = mmap.getValues(instance.getCPointer());
            for (Callback callback: values.toArray(new Callback[0])) {
                callback.unregister();
            }
        }
    }

    public synchronized void unregisterIf(String detailedCallback) {
        if (Objects.equals(detailedCallback, this.detailedCallback)) {
            unregister();
        }
    }

    public static void unregisterIf(Pointer instance, String detailedCallback) {
        synchronized (mmap) {
            var values = mmap.getValues(instance.getCPointer());
            for (Callback callback: values.toArray(new Callback[0])) {
                callback.unregisterIf(detailedCallback);
            }
        }
    }
}
