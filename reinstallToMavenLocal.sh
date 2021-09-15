#!/bin/sh


rm ${HOME}/.m2/repository/ch/bailu/java-gtk/library/0.1.0-SNAPSHOT/library-0.1.0-SNAPSHOT.jar
./gradlew publishToMavenLocal
