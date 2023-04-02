package ch.bailu.gtk.lib.bridge;

import java.io.IOException;
import java.util.HashMap;

import ch.bailu.gtk.gio.InputStream;
import ch.bailu.gtk.gio.InputStreamClass;
import ch.bailu.gtk.gobject.ObjectClassExtended;
import ch.bailu.gtk.type.Bytes;
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

    private final java.io.InputStream javaStream;

    private static HashMap<PointerContainer, java.io.InputStream> streams = new HashMap<>();

    public InputStreamBridge(PointerContainer cast) {
        super(cast);
        this.javaStream = streams.get(cast);
    }

    public InputStreamBridge(java.io.InputStream javaStream) {
        super(TypeSystem.newInstance(getTypeID()));
        System.out.println("InputStreamBridge()");

        this.javaStream = javaStream;
        streams.put(cast(), javaStream);
    }

    private static void initClass(InputStreamClass _class) {
        System.out.println("InputStreamBridge::classInit");

        ObjectClassExtended objectClassExtended = new ObjectClassExtended(_class.cast());
        objectClassExtended.setFieldDispose((__self, object) -> {
            new InputStreamBridge(object.cast()).onDispose();
            objectClassExtended.getParentClass().onDispose(object);

        });
        _class.setFieldReadFn((__self, stream, buffer, count, cancellable) -> new InputStreamBridge(stream.cast()).onRead(buffer, count));
        _class.setFieldCloseFn((__self, stream, cancellable) -> new InputStreamBridge(stream.cast()).onClose());
    }

    private void onDispose() {
        System.out.println("InputStreamBridge::onDispose");
        onClose();
        streams.remove(cast());
    }

    private boolean onClose() {
        System.out.println("InputStreamBridge::onClose");
        try {
            javaStream.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private long onRead(Pointer buffer, long count) {
        System.out.println("InputStreamBridge::onRead(" + count + ")");

        int read = 0;
        try {
            var bytesBuffer = new Bytes(buffer.cast());

            for (; read < count; read++) {
                int result = javaStream.read();
                if (result > -1) {
                    bytesBuffer.setByte(read, (byte) result);
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return read;
    }
}
