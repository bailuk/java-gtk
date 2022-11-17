package examples;

import ch.bailu.gtk.gio.Menu;
import ch.bailu.gtk.lib.bridge.ListIndex;
import ch.bailu.gtk.gtk.*;
import ch.bailu.gtk.lib.handler.CallbackHandler;
import ch.bailu.gtk.lib.handler.ClassHandler;
import ch.bailu.gtk.lib.handler.SignalHandler;
import ch.bailu.gtk.lib.handler.action.ActionHandler;
import ch.bailu.gtk.type.Str;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * https://gitlab.gnome.org/GNOME/gtk/-/issues/2971
 */
public class HugeList implements DemoInterface {
    private static final Str TITLE = new Str("Huge list");

    private final static File GIR_PATH = App.path("generator/src/main/resources/gir/");
    private final HashMap<String, Integer> wordList = new HashMap<>();

    long start = System.currentTimeMillis();
    long last  = System.currentTimeMillis();

    private final Application app;

    public HugeList(Application app) {
        this.app = app;
    }

    private int contextMenuIndex = 0;

    @Override
    public Window runDemo() {
        var window = new Window();
        window.setApplication(app); // For app.* actions
        window.setDefaultSize(640, 320);
        window.onShow(() -> log("window is about to show"));

        last = start = System.currentTimeMillis();
        log("start");
        var listIndex = new ListIndex();
        log("list index created");

        for (String name : GIR_PATH.list()) {
            File file = new File(GIR_PATH, name);
            readFileIntoList(file);
        }
        log("file read into list");

        List<String> keyList = new ArrayList<>(wordList.keySet());
        keyList.sort(Comparator.comparingInt(wordList::get).reversed());
        log("keys sorted");

        listIndex.setSize(wordList.size());
        log("listIndex has size");

        var factory = new SignalListItemFactory();

        var actionHandler = ActionHandler.get(app, "menu_selected");
        actionHandler.onActivate(() -> System.out.println("Menu of item " + contextMenuIndex + " selected"));

        window.onDestroy(actionHandler::disconnectSignals);

        factory.onSetup(item -> {
            var box = new Box(Orientation.HORIZONTAL, 5);
            box.append(createLabel());
            box.append(createLabel());

            var label = createLabel();
            label.setHexpand(true);
            box.append(label);

            var button = new Button();
            button.onDestroy(button::disconnectSignals); // Make sure all resources are getting released on tear down
            box.append(button);

            var menuButton = new MenuButton();
            var menu = new Menu();
            menu.append("Action", "app.menu_selected");
            menuButton.setMenuModel(menu);
            box.append(menuButton);

            var popover = menuButton.getPopover();
            popover.onDestroy(popover::disconnectSignals);
            new ListItem(item.cast()).setChild(box);
        });

        factory.onBind(item -> {
            var index = new Label(new ListItem(item.cast()).getChild().getFirstChild().cast());
            var count = new Label(index.getNextSibling().cast());
            var word = new Label(count.getNextSibling().cast());
            var button = new Button(word.getNextSibling().cast());
            var menuPopover = new MenuButton(button.getNextSibling().cast()).getPopover();

            var idx = ListIndex.toIndex(new ListItem(item.cast()));
            var key = keyList.get(idx);
            var cnt = wordList.get(key);

            // Reconnect "clicked" signal
            button.disconnectSignals(Button.SIGNAL_ON_CLICKED);
            if (idx == 0) {
                button.setLabel("Dump resources");
                button.onClicked(() -> {
                    ActionHandler.dump(System.out);
                    ClassHandler.dump(System.out);
                    CallbackHandler.dump(System.out);
                    SignalHandler.dump((System.out));
                });
            } else {
                button.setLabel(String.valueOf(idx));
                button.onClicked(() -> System.out.println("Button of item " + idx + " clicked"));
            }

            // Reconnect "show" signal
            menuPopover.disconnectSignals(Popover.SIGNAL_ON_SHOW);
            menuPopover.onShow(()-> contextMenuIndex = idx);

            setLabel(word, key);
            setLabel(count, String.valueOf(cnt));
            setLabel(index, String.valueOf(idx));
        });

        var list = new ListView(listIndex.inSelectionModel(), factory);
        log("list created");

        var scrolled = new ScrolledWindow();
        window.setChild(scrolled);
        scrolled.setChild(list);
        log("return window");
        return window;
    }

    private static void setLabel(Label label, String text) {
        Str str = new Str(text);
        label.setText(str);
        str.destroy();
    }

    public void readFileIntoList(File file) {
        try (var reader = new BufferedReader(new FileReader(file))) {
            readFromInputStream(reader);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void readFromInputStream(BufferedReader reader) throws IOException {
        for (String line; (line = reader.readLine()) != null;) {
            for (String word : line.split("[\"=/<>\\s]+")) {
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

    private Label createLabel() {
        var result = new Label(Str.NULL);
        result.setXalign(0);
        result.setWidthChars(7);
        result.setMarginEnd(10);
        return result;
    }

    @Override
    public Str getTitle() {
        return TITLE;
    }

    @Override
    public Str getDescription() {
        return TITLE;
    }

    public void log(String out) {
        var deltaStart = System.currentTimeMillis() - start;
        var deltaLast = System.currentTimeMillis() - last;
        System.out.println(deltaStart + " - " + deltaLast + ":> " + out);
        last = System.currentTimeMillis();
    }
}
