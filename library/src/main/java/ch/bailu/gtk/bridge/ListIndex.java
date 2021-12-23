package ch.bailu.gtk.bridge;

import ch.bailu.gtk.type.Pointer;

public class ListIndex extends Pointer {

    public ListIndex() {
        super(ImpListIndex.create());
    }

    public ListIndex(long size) {
        super(ImpListIndex.create());
        ImpListIndex.setSize(getCPointer(), size);
    }

    public long getPosition() {
        return ImpListIndex.getPosition(getCPointer());
    }

    public long getSize() {
        return ImpListIndex.getSize(getCPointer());
    }

    public void setSize(long size) {
        ImpListIndex.setSize(getCPointer(), size);
    }

    public void setPosition(long position) {
        ImpListIndex.setPosition(getCPointer(), position);
    }
}
