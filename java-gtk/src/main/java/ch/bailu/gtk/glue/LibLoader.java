package ch.bailu.gtk.glue;

import java.io.IOException;

public class LibLoader {

    public LibLoader() throws IOException {
        try {
            new LibResourceLoader().load();
        } catch(Exception e) {
            new SharedLibLoader().load();
        }
    }
}
