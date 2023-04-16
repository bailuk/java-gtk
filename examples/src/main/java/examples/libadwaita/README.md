# libadwaita demo

## [demo_simple](demo_simple)

Simple POC adwaita demo ported from [https://github.com/Northshore-Hero/libadwaita-demo](https://github.com/Northshore-Hero/libadwaita-demo)


## [demo](demo)

Almost complete port of the official libadwaita demo [https://gitlab.gnome.org/GNOME/libadwaita/-/blob/main/demo](https://gitlab.gnome.org/GNOME/libadwaita/-/blob/main/demo)

Demonstrates how typical patterns can be implemented in Java.

Uses convenience methods from `java-gtk/src/main/java/ch/bailu/gtk/type/gobject/TypeSystem.java`,
`java-gtk/src/main/java/ch/bailu/gtk/gobject/ObjectClassExtended.java` and `java-gtk/src/main/java/ch/bailu/gtk/gtk/WidgetClassExtended.java`

*Resource loading* is done directly via Java Resources where possible and indirectly 
from a gresource file located in the `java/resources` directory file as a workaround.

*Overloading of virtual functions* is not yet supported by the generated API.

Demo can be executed by running the main function located here: [demo/AdwaitaDemo.java](demo/AdwaitaDemo.java)
