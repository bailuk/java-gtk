package ch.bailu.gtk.lib.handler;

import java.util.Objects;

import ch.bailu.gtk.lib.util.IDGen;
import ch.bailu.gtk.lib.util.MMap;
import ch.bailu.gtk.lib.util.SizeLog;
import ch.bailu.gtk.type.Pointer;

public class CallbackHandler {
    private static final IDGen idGen = new IDGen();
    private final Pointer instance;
    private final String methodName;
    private com.sun.jna.Callback callback;
    private final long callbackId = idGen.gen();

    private static final MMap<Long, Long, CallbackHandler> mmap = new MMap<>();
    private static final SizeLog sizeLog = new SizeLog(CallbackHandler.class.getSimpleName());

    public CallbackHandler(Pointer instance, String methodName) {
        this.instance = instance;
        this.methodName = methodName;
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
            for (CallbackHandler callback: values.toArray(new CallbackHandler[0])) {
                callback.unregister();
            }
        }
    }

    public synchronized void unregister(String detailedCallback) {
        if (Objects.equals(detailedCallback, this.methodName)) {
            unregister();
        }
    }

    public static void unregister(Pointer instance, String detailedCallback) {
        synchronized (mmap) {
            var values = mmap.getValues(instance.getCPointer());
            for (CallbackHandler callback: values.toArray(new CallbackHandler[0])) {
                callback.unregister(detailedCallback);
            }
        }
    }
}
