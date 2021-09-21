package ch.bailu.gtk;

import org.junit.jupiter.api.Test;

import ch.bailu.gtk.wrapper.Bytes;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestBytes {


    static {
        System.loadLibrary("glue");
    }

    @Test
    public void testBytes() {
        byte[] buffer = new byte[200];
        buffer[0]=42;
        buffer[199]=99;

        Bytes bytesA = new Bytes(buffer);


        assertEquals(200, bytesA.getSize());
        assertEquals(42, bytesA.getByte(0));
        assertEquals(99, bytesA.getByte(199));
        bytesA.destroy();
        assertEquals(0, bytesA.getSize());


        Bytes bytesB = new Bytes(new byte[0]);
        assertEquals(0, bytesB.getSize());
        bytesB.destroy();

    }


}
