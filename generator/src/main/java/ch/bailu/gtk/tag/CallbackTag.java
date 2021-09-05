package ch.bailu.gtk.tag;

import java.io.IOException;

public class CallbackTag extends MethodTag {
    public CallbackTag(Tag parent) {
        super(parent);
    }

    @Override
    public void end() throws IOException {
        getBuilder().buildCallback(this);
    }

}
