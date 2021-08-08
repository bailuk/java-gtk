package ch.bailu.gtk.model;

public abstract class Model {
    private boolean supported = true;
    private String supportedState = "Supported";

    public boolean isSupported() {
        return supported;
    }

    public void setSupported(String reason, boolean supported) {
        if (!supported) {
            this.supported = false;
            this.supportedState = reason;
        }
    }

    public String getSupportedState() {
        return supportedState;
    }
}
