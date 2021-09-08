# Java-GTK
Experimental GTK 3 bindings for java

## Example
```java
package examples;

import ch.bailu.gtk.gio.ApplicationFlags;
import ch.bailu.gtk.gtk.Application;
import ch.bailu.gtk.gtk.ApplicationWindow;
import ch.bailu.gtk.gtk.Button;

public class HelloWorld {
    public HelloWorld(String[] argv) {
        var app = new Application("com.example.GtkApplication",
                ApplicationFlags.FLAGS_NONE);

        app.onActivate(() -> {
            // Create a new window
            var window = new ApplicationWindow(app);

            // Create a new button
            var button = Button.newWithLabelButton("Hello, World!");

            // When the button is clicked, close the window
            button.onClicked(() -> window.close());
            window.add(button);
            window.showAll();
        });

        app.run(argv.length, argv);
    }
}
```

## Build
`./gradlew generate`
Compiles and runs the code generator. This will generate Java and C code from [GIR](https://gi.readthedocs.io/en/latest/) files.
Input: `generator/src/resources/gir/*`
Output Java: `library/build/generated/src/main/java/[...]/*.java`
Output C: `glue/build/generated/src/main/c/*.c`
Configuration: [generator/src/main/java/ch/bailu/gtk/Configuration.java](generator/src/main/java/ch/bailu/gtk/Configuration.java)

`./gradlew run`
Compile everything and run the default demo application.
The default demo application can be selected in [examples/src/main/java/examples/App.java](examples/src/main/java/examples/App.java)

`./gradlew publishToMavenLocal`
Compile Java and C library, generate JAR archive and copy JAR archive as artifact to local Maven repository (`~/.m2/repository`).

 
 ## Modules
 | `generator/` | Java/Kotlin application that generates C and Java code from GIR files (xml parser -> model builder -> writer). GIR files are taken from Debian dev packages.
 | `library/`   | java-gtk library depends on generated Java code.
 | `glue/`      | JNI C-Library. Depends on generated C code  .
 | `examples/`  | Some examples to test the bindings. Mostly ported from [https://gitlab.gnome.org/GNOME/gtk/-/tree/gtk-3-24/demos/gtk-demo](https://gitlab.gnome.org/GNOME/gtk/-/tree/gtk-3-24/demos/gtk-demo).
 
 ## License
 - Files in [generator/src/resources/gir/](generator/src/resources/gir/) are comming from the [GTK project](https://gitlab.gnome.org/GNOME/gtk) and are therefore licensed under the GNU Library General Public License.
 - Examples in [examples/src/main/java/examples](examples/src/main/java/examples) are ported from the [GTK project](https://gitlab.gnome.org/GNOME/gtk/-/tree/gtk-3-24/demos/gtk-demo) and are therefore licensed under the GNU Library General Public License.   
 - Everything else licensed under the [MIT License](https://en.wikipedia.org/wiki/MIT_License)