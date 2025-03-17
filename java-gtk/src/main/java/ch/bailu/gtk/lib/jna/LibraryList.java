package ch.bailu.gtk.lib.jna;

import java.util.HashMap;
import java.util.TreeSet;

public class LibraryList {
    private final HashMap<String, TreeSet<String>> libraries = new HashMap<>();

    public boolean addInitial(String libraryName) {
        if (!libraries.containsKey(libraryName)) {
            addSingle(libraryName, libraryName);
            return true;
        }
        return false;
    }

    public void addSingle(String libraryName, String lib) {
        TreeSet<String> set = new TreeSet<>();
        set.add(lib);
        libraries.put(libraryName, set);
    }

    public TreeSet<String> get(String libraryName) {
        return libraries.get(libraryName);
    }

    public void clear(String libraryName) {
        libraries.put(libraryName, new TreeSet<>());
    }

    public void add(String libraryName, String lib) {
        if (libraries.containsKey(libraryName)) {
            libraries.get(libraryName).add(lib);
        }
    }

    public int size(String libraryName) {
        if (libraries.containsKey(libraryName)) {
            return libraries.get(libraryName).size();
        }
        return 0;
    }
}
