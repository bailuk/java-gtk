# Using java-gtk on MacOS

## Install GTK libraries

[Setting up GTK for Mac OS ](https://www.gtk.org/docs/installations/macos/)

## Running a java-gtk application

On MacOS the main thread of a java-gtk application needs to be thread 0.
In order to start the application on the correct thread the option `-XstartOnFirstThread` needs to be passed to the JVM

Example: `./gradlew run -Dorg.gradle.jvmargs="-XstartOnFirstThread"`
