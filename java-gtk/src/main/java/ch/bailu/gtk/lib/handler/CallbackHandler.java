package ch.bailu.gtk.lib.handler;

import java.io.PrintStream;
import java.util.Objects;

import ch.bailu.gtk.lib.util.IDGen;
import ch.bailu.gtk.lib.util.MMap;
import ch.bailu.gtk.lib.util.SizeLog;
import ch.bailu.gtk.type.Pointer;

/**
 * Handler to callback resource.
 * Stores java reference to callback in a hash map
 * Provides function to free reference to callback
 */
public class CallbackHandler {
    private static final IDGen idGen = new IDGen();

    private com.sun.jna.Callback callback;

    private final Pointer instance;
    private final String methodName;
    private final long callbackId = idGen.gen();

    private static final MMap<Long, Long, CallbackHandler> mmap = new MMap<>();
    private static final SizeLog sizeLog = new SizeLog(CallbackHandler.class.getSimpleName());

    public CallbackHandler(Pointer instance, String methodName) {
        this.instance = instance;
        this.methodName = methodName;
    }

    /**
     * For internal use (gets called after callback was passed to a C function)
     * @param callback (lambda)
     */
    public synchronized void register(com.sun.jna.Callback callback)  {
        if (this.callback == null) {
            this.callback = callback;
            mmap.put(instance.getCPointer(), callbackId, this);
            sizeLog.log(mmap.size());
        }
    }

    /**
     * Unregister this callback
     * Removes java reference to callback
     * Only call this if this callbacks will never called again
     */
    public synchronized void unregister() {
        if (this.callback != null) {
            mmap.remove(instance.getCPointer(), callbackId);
            this.callback = null;
        }
    }

    /**
     * Unregister all callbacks of this instance
     * Removes java reference to callback
     * Only call this if this callbacks will never called again
     * @param instance
     */
    public static void unregister(Pointer instance) {
        synchronized (mmap) {
            var values = mmap.getValues(instance.getCPointer());
            for (CallbackHandler callback: values.toArray(new CallbackHandler[0])) {
                callback.unregister();
            }
        }
    }

    /**
     * Unregister this callback if its methodName equals methodName
     * Removes java reference to callback
     * Only call this if this callbacks will never called again
     * @param methodName string identifier (name)
     */
    public synchronized void unregister(String methodName) {
        if (Objects.equals(methodName, this.methodName)) {
            unregister();
        }
    }

    public static void unregister(Pointer instance, String methodName) {
        synchronized (mmap) {
            var values = mmap.getValues(instance.getCPointer());
            for (CallbackHandler callback: values.toArray(new CallbackHandler[0])) {
                callback.unregister(methodName);
            }
        }
    }

    /**
     * Get instance this callback ist registered to
     * Only call this if this callbacks will never called again
     * @return instance this callback belongs to
     */
    public Pointer getInstance() {
        return instance;
    }

    /**
     * Name of method this callback was registered with
     * @return methodName
     */
    public String getMethodName() {
        return  methodName;
    }


    /**
     * Dump resources (contents of internal map as text) to stream
     * @param out stream
     */
    public static void dump(PrintStream out) {
        out.println("_");
        out.println(CallbackHandler.class.getSimpleName());
        out.println("=".repeat(CallbackHandler.class.getSimpleName().length()));
        out.print(mmap.size());

        var keySet = mmap.keySet();
        out.print(" in ");
        out.println(keySet.size());

        keySet.forEach((Long key)->{
            out.println();
            out.print(Long.toHexString(key));
            out.print(" has ");

            var values = mmap.getValues(key);
            out.println(values.size());
            values.forEach(out::println);
        });
    }


    @Override
    public String toString() {
        return callbackId + " " + methodName;
    }
}
