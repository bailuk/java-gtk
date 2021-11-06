#!/bin/sh

echo "Remove 'java-gtk' from local Maven repository"
rm -r ${HOME}/.m2/repository/ch/bailu/java-gtk/

echo "Install library to local Maven repository"
./gradlew publishToMavenLocal
