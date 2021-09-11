package ch.bailu.gtk.tag;

import java.util.ArrayList;

public class TagList<T extends Tag> extends ArrayList<T> {

    public Tag addTag(T tag) {
        this.add(tag);
        return tag;
    }

}
