package ch.bailu.gtk.type.exception;

public class AllocationError extends Exception {

    public AllocationError(String className) {
        super(className + ": allocation failed");
    }

}
