#!/bin/sh


rm ${HOME}/.m2/repository/ch/bailu/java-gtk/java-gtk/0.1/java-gtk-0.1.jar
./gradlew publishToMavenLocal
