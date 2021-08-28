package ch.bailu.gtk.writer;

import java.io.IOException;
import java.io.Writer;

public class GroupSpace {
    private int count = 0;
    private boolean print = false;
    private final Writer out;

    public GroupSpace(Writer out) {
        this.out = out;
    }

    public void start(int c) throws IOException {
        end(c);
        start();
    }

    public void end(int c) {
        count = Math.max(c, count);
    }

    public void next() {
        print = true;
    }

    public void start() throws IOException {
        if (print) {
            out.append("\n".repeat(count));
            count = 0;
            print = false;
        }
    }
}
