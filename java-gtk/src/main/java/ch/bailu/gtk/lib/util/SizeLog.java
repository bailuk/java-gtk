package ch.bailu.gtk.lib.util;

public class SizeLog {

    private final String label;
    private long lastLog = System.currentTimeMillis();
    private long lastSize = 0;

    public SizeLog(String label) {
        this.label = label;
    }

    public void log(long size) {
        long now = System.currentTimeMillis();

        if (now - lastLog > 5000) {
            if (lastSize != size) {
                System.out.println("R::" + this.label +  ": " + size);
            }
            lastLog = now;
            lastSize = size;
        }
    }
}
