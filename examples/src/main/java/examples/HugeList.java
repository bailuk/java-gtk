package examples;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import ch.bailu.gtk.bridge.ListIndex;
import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.ApplicationWindow;
import ch.bailu.gtk.gtk.Box;
import ch.bailu.gtk.gtk.Label;
import ch.bailu.gtk.gtk.ListView;
import ch.bailu.gtk.gtk.Orientation;
import ch.bailu.gtk.gtk.ScrolledWindow;
import ch.bailu.gtk.gtk.SignalListItemFactory;
import ch.bailu.gtk.helper.LabelHelper;
import ch.bailu.gtk.type.Str;

/**
 * https://gitlab.gnome.org/GNOME/gtk/-/issues/2971
 */
public class HugeList {
    private final static File GIR_PATH = App.path("generator/src/main/resources/gir/");

    private final HashMap<String, Integer> wordList = new HashMap<>();

    public HugeList() {
        Application app = new Application(App.ID, ApplicationFlags.FLAGS_NONE);

        app.onActivate(() -> {
            var window = new ApplicationWindow(app);
            window.setTitle(new Str("Huge List"));
            window.setDefaultSize(640,320);

            var listIndex = new ListIndex();

            for (String name: GIR_PATH.list()) {
                File file = new File(GIR_PATH, name);
                readFileIntoList(file);
            }

            List<String> keyList = new ArrayList<>(wordList.keySet());
            keyList.sort(Comparator.comparingInt(wordList::get).reversed());
            listIndex.setSize(wordList.size());

            var factory = new SignalListItemFactory();

            factory.onSetup(item->{
                var box = new Box(Orientation.HORIZONTAL,5);
                box.append(createLabel());
                box.append(createLabel());
                box.append(createLabel());

                item.setChild(box);
            });

            factory.onBind(item->{
                var index = new Label(item.getChild().getFirstChild().cast());
                var count = new Label(index.getNextSibling().cast());
                var word  = new Label(count.getNextSibling().cast());

                var idx = ListIndex.toIndex(item);
                var key = keyList.get(idx);
                var cnt = wordList.get(key);

                LabelHelper.setLabel(word,  key);
                LabelHelper.setLabel(count, String.valueOf(cnt));
                LabelHelper.setLabel(index, String.valueOf(idx));

            });

            var list = new ListView(listIndex.inSelectionModel(), factory);

            var scrolled = new ScrolledWindow();
            window.setChild(scrolled);
            scrolled.setChild(list);
            window.show();
        });

        app.run(0, null);
    }

    public void readFileIntoList(File file) {
        InputStream input = null;
        try  {
            input = new FileInputStream(file);
            readFromInputStream(input);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            closeStream(input);
        }
    }

    private void readFromInputStream(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            for (String word: line.split("[\"=/<>\\s]+")) {
                String w = word.strip();
                if (w.length() > 0) {
                    wordList.put(w, getWordCount(w) + 1);
                }
            }
        }
    }

    private int getWordCount(String word) {
        Integer count = wordList.get(word);
        return (count == null) ? 0 : count;
    }

    private void closeStream(InputStream input) {
        if (input != null) {
            try {
                input.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private Label createLabel() {
        var result = new Label(new Str(""));
        result.setXalign(0);
        result.setWidthChars(7);
        result.setMarginEnd(10);
        return result;
    }
}
