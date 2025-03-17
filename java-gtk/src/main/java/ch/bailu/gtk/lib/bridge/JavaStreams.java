package ch.bailu.gtk.lib.bridge;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import ch.bailu.gtk.type.Bytes;
import ch.bailu.gtk.type.Pointer;
import ch.bailu.gtk.type.PointerContainer;

class JavaStreams {

    private static final HashMap<PointerContainer, InputStream> STREAMS = new HashMap<>();

    public long read(PointerContainer key, Pointer buffer, long count) {
        var javaStream = getStream(key);
        long result = 0;
        if (javaStream != null) {
            result =  read(javaStream, buffer, count);
        }
        return result;
    }

    private long read(InputStream javaStream, Pointer buffer, long count) {
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

    public void close(PointerContainer key) throws IOException {
        var javaStream = getStream(key);
        if (javaStream != null) {
            STREAMS.remove(key);
            javaStream.close();
        }
    }

    public void put(PointerContainer key, InputStream javaStream) {
        STREAMS.put(key, javaStream);
    }

    private InputStream getStream(PointerContainer key) {
        var result = STREAMS.get(key);
        if (result == null) {
            System.err.println("Stream is already closed");
        }
        return result;
    }
}
