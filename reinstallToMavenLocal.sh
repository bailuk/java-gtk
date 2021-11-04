#!/bin/sh

rm -r ${HOME}/.m2/repository/ch/bailu/java-gtk/
./gradlew publishToMavenLocal
