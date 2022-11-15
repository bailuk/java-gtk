package ch.bailu.gtk.lib.handler;

import com.sun.jna.Callback;

import java.io.PrintStream;
import java.util.Objects;

import ch.bailu.gtk.lib.GObject;
import ch.bailu.gtk.lib.util.MMap;
import ch.bailu.gtk.lib.util.SizeLog;
import ch.bailu.gtk.type.Pointer;

public class SignalHandler {
    private final Callback callback;
    private final long handlerId;
    private final String detailedSignal;
    private final Pointer instance;

    private static final MMap<Long, Long, SignalHandler> mmap = new MMap<>();
    private static final SizeLog sizeLog = new SizeLog(SignalHandler.class.getSimpleName());

    public SignalHandler(Pointer instance, String detailedSignal, Callback callback) {
        this.callback = callback;
        this.instance = instance;
        this.detailedSignal = detailedSignal;
        this.handlerId = GObject.INST().g_signal_connect_data(instance.getCPointer(), detailedSignal, this.callback, 0, 0, 0);

        mmap.put(instance.getCPointer(), handlerId, this);
        sizeLog.log(mmap.size());
    }

    public synchronized void disconnectIf(String detailedSignal) {
        if (Objects.equals(detailedSignal, this.detailedSignal)) {
            disconnect();
        }
    }

    public synchronized void disconnect() {
        GObject.INST().g_signal_handler_disconnect(instance.getCPointer(), handlerId);
        mmap.remove(instance.getCPointer(), handlerId);
    }

    public static void disconnect(Pointer instance) {
        synchronized (mmap) {
            var values = mmap.getValues(instance.getCPointer());
            for (SignalHandler signal: values.toArray(new SignalHandler[0])) {
                signal.disconnect();
            }
        }
    }

    public static void disconnect(Pointer instance, String detailedSignal) {
        synchronized (mmap) {
            var values = mmap.getValues(instance.getCPointer());
            for (SignalHandler signal: values.toArray(new SignalHandler[0])) {
                signal.disconnectIf(detailedSignal);
            }
        }
    }

    @Override
    public String toString() {
        return handlerId + " " + detailedSignal;
    }

    public static void dump(PrintStream out) {
        out.println("_");
        out.println(SignalHandler.class.getSimpleName());
        out.println("=".repeat(SignalHandler.class.getSimpleName().length()));
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

}
