package ch.bailu.gtk.lib.bridge;

import java.io.IOException;

import ch.bailu.gtk.gio.InputStream;
import ch.bailu.gtk.gio.InputStreamClass;
import ch.bailu.gtk.type.Pointer;
import ch.bailu.gtk.type.PointerContainer;
import ch.bailu.gtk.type.gobject.TypeSystem;

public class InputStreamBridge extends InputStream {
    private static long type = 0;

    public synchronized static long getTypeID() {
        if (type == 0) {
            type = TypeSystem.registerClass(
                    InputStream.getTypeID(),
                    InputStreamBridge.class,
                    0,
                    (__self, g_class, class_data) -> initClass(new InputStreamClass(g_class.cast())),
                    (__self, instance, g_class) -> System.out.println("InputStreamBridge init instance"));
        }
        return type;
    }

    private static final JavaStreams STREAMS = new JavaStreams();

    public InputStreamBridge(PointerContainer cast) {
        super(cast);
    }

    public InputStreamBridge(java.io.InputStream javaStream) {
        super(TypeSystem.newInstance(getTypeID()));
        System.out.println("InputStreamBridge()");

        STREAMS.put(cast(), javaStream);
    }

    private static void initClass(InputStreamClass _class) {
        System.out.println("InputStreamBridge::classInit");

        _class.setFieldReadFn((__self, stream, buffer, count, cancellable) -> new InputStreamBridge(stream.cast()).onRead(buffer, count));
        _class.setFieldCloseFn((__self, stream, cancellable) -> new InputStreamBridge(stream.cast()).onClose());
    }

    private boolean onClose() {
        System.out.println("InputStreamBridge::onClose");
        try {
            STREAMS.close(cast());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private long onRead(Pointer buffer, long count) {
        System.out.println("InputStreamBridge::onRead(" + count + ")");
        return STREAMS.read(cast(), buffer, count);
    }
}
