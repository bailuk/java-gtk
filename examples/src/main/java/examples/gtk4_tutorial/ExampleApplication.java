package examples.gtk4_tutorial;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Set;

import ch.bailu.gtk.GTK;
import ch.bailu.gtk.exception.AllocationError;
import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gio.MenuModel;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.Button;
import ch.bailu.gtk.gtk.Dialog;
import ch.bailu.gtk.gtk.Editable;
import ch.bailu.gtk.gtk.ListBox;
import ch.bailu.gtk.gtk.MenuButton;
import ch.bailu.gtk.gtk.Revealer;
import ch.bailu.gtk.gtk.ScrolledWindow;
import ch.bailu.gtk.gtk.SearchBar;
import ch.bailu.gtk.gtk.SearchEntry;
import ch.bailu.gtk.gtk.Stack;
import ch.bailu.gtk.gtk.TextBuffer;
import ch.bailu.gtk.gtk.TextIter;
import ch.bailu.gtk.gtk.TextSearchFlags;
import ch.bailu.gtk.gtk.TextView;
import ch.bailu.gtk.gtk.ToggleButton;
import ch.bailu.gtk.gtk.Window;
import ch.bailu.gtk.helper.ActionHelper;
import ch.bailu.gtk.helper.BuilderHelper;
import ch.bailu.gtk.type.Str;

/**
 * https://docs.gtk.org/gtk4/getting_started.html
 */
public class ExampleApplication {

    private final static Str APP_ID = new Str("org.gtk.example");
    private final static String BASE_PATH = "src/main/java/examples/gtk4_tutorial/";

    public ExampleApplication() {
        final var app = new Application(APP_ID, ApplicationFlags.FLAGS_NONE);

        app.onActivate(() -> {
            try {
                var appBuilder = new BuilderHelper(BASE_PATH,"window.ui");
                var mnuBuilder = new BuilderHelper(BASE_PATH,"menu.ui");
                var dlgBuilder = new BuilderHelper(BASE_PATH,"preferences.ui");

                var menuModel = new MenuModel(mnuBuilder.getObject("menu"));
                var menuButton = new MenuButton(appBuilder.getObject("gears"));

                menuButton.setMenuModel(menuModel);

                var window = new Window(appBuilder.getObject("window"));
                window.setApplication(app);

                var actions = new ActionHelper(app);
                actions.add("quit", (parameter)->app.quit());
                actions.setAccels("quit", new String[]{"<Ctrl>Q", null});

                actions.add("preferences",(parameter)->{
                    var dialog = new Dialog(dlgBuilder.getObject("preferences"));
                    dialog.setTransientFor(window);
                    dialog.present();
                });

                actions.add("show-words", false, (parameter) ->{
                    var revealer = new Revealer(appBuilder.getObject("sidebar"));
                    revealer.setRevealChild(GTK.IS(actions.getBooleanState("show-words")));
                });

                var stack = new Stack(appBuilder.getObject("stack"));


                for (File file: new File(BASE_PATH).listFiles()) {
                    try {
                        open(stack, file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                var searchToggle = new ToggleButton(appBuilder.getObject("search"));
                var searchEntry = new SearchEntry(appBuilder.getObject("searchentry"));
                var searchBar = new SearchBar(appBuilder.getObject("searchbar"));

                searchToggle.onToggled(()-> searchBar.setSearchMode(searchToggle.getActive()));

                stack.onNotify(pspec -> {
                    System.out.println(pspec.getName().toString());
                    if ("visible-child".equals(pspec.getName().toString())) {
                        if (stack.inDestruction() == GTK.FALSE) {
                            searchBar.setSearchMode(GTK.FALSE);
                            updateWords(stack, new ListBox(appBuilder.getObject("words")), new Editable(searchEntry.cast()));
                        }
                    }
                });

                searchEntry.onSearchChanged(() -> {
                    Str text = new Editable(searchEntry.cast()).getText();
                    var tab = new ScrolledWindow(stack.getVisibleChild().cast());
                    var view = new TextView(tab.getChild().cast());
                    var buffer = getCurrentBuffer(stack);
                    var start = new TextIter();
                    var match_start = new TextIter();
                    var match_end = new TextIter();

                    buffer.getStartIter(start);
                    if (GTK.IS(start.forwardSearch(text, TextSearchFlags.CASE_INSENSITIVE, match_start, match_end, null))) {
                        buffer.selectRange(match_start, match_end);
                        view.scrollToIter(match_start,0.0, GTK.FALSE, 0.0, 0.0);
                    }

                    match_end.destroy();
                    match_start.destroy();
                    start.destroy();
                });


                updateWords(stack, new ListBox(appBuilder.getObject("words")), new Editable(searchEntry.cast()));
                window.show();

            } catch (AllocationError e) {
                System.err.println(e.getMessage());
                app.quit();
            }
        });

        app.run(0, null);
        app.unref();
    }

    private TextBuffer getCurrentBuffer(Stack stack) {
        var tab = new ScrolledWindow(stack.getVisibleChild().cast());
        var view = new TextView(tab.getChild().cast());
        return view.getBuffer();
    }

    private void open(Stack stack, File file) throws IOException {
        var name = new Str(file.getName());

        var scrolled = new ScrolledWindow();
        scrolled.setHexpand(GTK.TRUE);
        scrolled.setVexpand(GTK.TRUE);

        var text = new TextView();
        text.setEditable(GTK.FALSE);
        text.setCursorVisible(GTK.FALSE);
        scrolled.setChild(text);
        stack.addTitled(scrolled, name, name);

        var content = new Str(Files.readString(Path.of(file.getAbsolutePath())));
        text.getBuffer().setText(content, -1);
        content.destroy();
    }


    private void updateWords(Stack stack, ListBox words, Editable entry) {
        var list = getWordsList(getCurrentBuffer(stack));

        var child = words.getFirstChild();
        while (child.isNotNull()) {
            //var text = new Button(child.cast()).getLabel();
            words.remove(child);
            //text.destroy();

            child = words.getFirstChild();
        }

        for (String w: list) {
            var word = new Str(w);
            var row = Button.newWithLabelButton(word);
            row.onClicked(() -> entry.setText(word));
            words.insert(row, -1);
        }
    }

    private Set<String> getWordsList(TextBuffer buffer) {
        var list = new HashMap<String, Integer>();

        var start = new TextIter();
        var end = new TextIter();

        buffer.getStartIter(start);
        buffer.getStartIter(end);

        while (!GTK.IS(start.isEnd())) {
            while (!GTK.IS(start.startsWord()) && GTK.IS(start.forwardChar()) && GTK.IS(end.forwardChar()));

            if (!GTK.IS(end.forwardWordEnd())) {
                break;
            }

            Str word = buffer.getText(start, end, GTK.FALSE);
            list.put(word.toString(), 0);
            word.destroy();

            if (!GTK.IS(start.forwardWordEnd())) {
                break;
            }
        }
        return list.keySet();
    }
}
