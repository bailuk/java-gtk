package ch.bailu.gtk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import jnr.ffi.LibraryLoader;
import jnr.ffi.Pointer;
import jnr.ffi.Runtime;
import jnr.ffi.Struct;

public class TestJnrFfi {
    public interface LibC { // A representation of libC in Java
        int puts(String s); // mapping of the puts function, in C `int puts(const char *s);`
    }


    public static class GString extends Struct {
        public Struct.String str;
        public Struct.size_t len;
        public Struct.size_t allocated_len;

        public GString(Runtime runtime) {
            super(runtime);
        }
    }


    public interface Glib {
        String g_get_application_name();
    }

    public interface Glib2 {
        void g_set_application_name(String str);
        void g_print(String str, String other);
    }

    @Test
    public void testHelloWorld() {

        LibC libc = LibraryLoader.create(LibC.class).load("c"); // load the "c" library into the libc variable
        libc.puts("Hello World!"); // prints "Hello World!" to console
    }

    @Test
    public void testHelloGlib() {

        Glib glib = LibraryLoader.create(Glib.class).load("glib-2.0"); // load the "c" library into the libc variable

        Glib2 glib2 = LibraryLoader.create(Glib2.class).load("glib-2.0"); // load the "c" library into the libc variable
        glib2.g_set_application_name("Test app");
        glib2.g_print("%s", glib.g_get_application_name());


    }

}
