package ch.bailu.gtk.lib.jna;

import java.util.HashMap;
import java.util.TreeSet;
import java.util.function.Consumer;

public class LibraryList {
    private HashMap<String, TreeSet<String>> libraries = new HashMap<>();

    public void addInitial(String libraryName) {
        if (!libraries.containsKey(libraryName)) {
            addSingle(libraryName, libraryName);
        }
    }

    public void forEach(String libraryName, Consumer<String> action) {
        if (libraries.containsKey(libraryName)) {
            libraries.get(libraryName).forEach(action);
        }
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
}
