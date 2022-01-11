package com.example;

import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.ApplicationWindow;
import ch.bailu.gtk.gtk.Button;
import ch.bailu.gtk.type.Str;
import ch.bailu.gtk.type.Strs;

import java.io.IOException;

import ch.bailu.gtk.GTK;

public class App {
    public static void main(String[] args) throws IOException {
        GTK.init();
        new App(args);
    }

    public App(String[] args) {
        var app = new Application(new Str("com.example.GtkApplication"),
            ApplicationFlags.FLAGS_NONE);

        app.onActivate(() -> {
            
            // Create a new window
            var window = new ApplicationWindow(app);

            // Create a new button
            var button = Button.newWithLabelButton(new Str("Hello, World!"));

            // When the button is clicked, close the window
            button.onClicked(() -> window.close());
            window.setChild(button);
            window.show();
        });

        app.run(args.length, new Strs(args));
    }
}