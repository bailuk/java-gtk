package ch.bailu.gtk;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.bailu.gtk.bridge.Bytes;

public class TestGBytes {

    @Test
    public void testBytes() {
        byte[] buffer = new byte[200];
        buffer[0]=42;
        buffer[199]=99;

        Bytes bytesA = new Bytes(buffer);


        assertEquals(200, bytesA.getSize());
        assertEquals(42, bytesA.getByte(0));
        assertEquals(99, bytesA.getByte(199));
        bytesA.unref();
        assertEquals(0, bytesA.getSize());


        Bytes bytesB = new Bytes(new byte[0]);
        assertEquals(0, bytesB.getSize());
        bytesB.unref();


        Bytes bytesC = new Bytes(new byte[]{4,5,0});
        byte[] res = bytesC.toBytes();
        assertEquals(3, res.length);
        assertEquals(4, res[0]);
        assertEquals(5, res[1]);
        assertEquals(0, res[2]);
        bytesC.unref();
    }

}
