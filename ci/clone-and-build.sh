#!/bin/sh

test -d java-gtk || git clone "https://github.com/bailuk/java-gtk.git"
cd java-gtk || exit 1
git pull && git checkout gtk4

./regenerateSources.sh && ./buildLibrary.sh $1 && ./reinstallToMavenLocal.sh && ./gradlew examples:build
