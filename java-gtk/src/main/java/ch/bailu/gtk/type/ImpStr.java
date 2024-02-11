package ch.bailu.gtk.type;

import java.nio.charset.StandardCharsets;

class ImpStr {
    public static String toString(long pointer) {
        return Pointer.asJnaPointer(pointer).getString(0, StandardCharsets.UTF_8.name());
    }
}
