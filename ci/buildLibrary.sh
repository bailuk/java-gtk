#!/bin/sh

# use `make` instead
#
# compile and build library

test -f gradlew || cd ..

echo
echo "Build java archives"
./gradlew -q java-gtk:build || exit 1
find java-gtk/ -name *.jar
