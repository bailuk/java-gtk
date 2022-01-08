#!/bin/sh

# Removes directory 'javadoc/' and
# installs Java Doc to directory 'javadoc/'

echo "\nClean Java Doc"
rm -r javadoc

echo "\nGenerate Java Doc"
mkdir javadoc || exit 1
./gradlew -q java-gtk:javadocJar || exit 1
unzip -q java-gtk/build/libs/library-0.1.0-SNAPSHOT-javadoc.jar -d javadoc || exit 1

find javadoc/ -name index.html
