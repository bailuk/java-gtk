package ch.bailu.gtk.glue;

import java.io.IOException;

public class LibLoader {

    public LibLoader() throws IOException, UnsatisfiedLinkError {
        try {
            new SharedLibLoader();
        } catch(Throwable e) {
            new LibResourceLoader();
        }
    }
}
