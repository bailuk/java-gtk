package ch.bailu.gtk;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.bailu.gtk.type.Bytes;
import ch.bailu.gtk.type.PointerContainer;

public class TestBytes {


    @Test
    public void testBytes() {
        byte[] buffer = new byte[200];
        buffer[0]=42;
        buffer[199]=99;

        Bytes bytesA = new Bytes(buffer);


        assertEquals(200, bytesA.getSize());
        assertEquals(42, bytesA.getByte(0));
        assertEquals(99, bytesA.getByte(199));

        bytesA.setByte(199, (byte) 98);
        assertEquals(98, bytesA.getByte(199));

        bytesA.destroy();
        assertEquals(0, bytesA.getSize());


        Bytes bytesB = new Bytes(new byte[0]);
        assertEquals(0, bytesB.getSize());
        bytesB.destroy();
    }

    @Test
    public void testNullBytes() {
        Bytes bytes = new Bytes(PointerContainer.NULL);
        assertEquals(0, bytes.getSize());
        assertEquals(0, bytes.getLength());
    }
}
