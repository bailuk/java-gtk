package ch.bailu.gtk.model;

public abstract class Model {
    private boolean supported = true;

    public boolean isSupported() {
        return supported;
    }

    public void setSupported(boolean supported) {
        if (!supported) {
            this.supported = false;
        }
    }
}
