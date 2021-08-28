package ch.bailu.gtk.tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EnumerationTag extends TypeTag {

    private final List<MemberTag> members = new ArrayList<>(5);

    private String type = "";

    public EnumerationTag(Tag parent) {
        super(parent);
    }

    @Override
    public Tag getChild(String name, String prefix) {
        if ("member".equals(name)) {
            return add(members, new MemberTag(this));
        }
        return super.getChild(name, prefix);
    }

    @Override
    public void end() throws IOException {
        getBuilder().buildEnumeration(this);
    }

    public List<MemberTag> getMembers() {
        return members;
    }
}
