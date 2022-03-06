#!/bin/sh

# use `make gen` instead
#
# clean library module and glue module
# compile and run source generator


test -f gradlew || cd ..

echo
echo "Clean all"
./gradlew -q java-gtk:clean || exit 1

echo
echo "Generate sources"
./gradlew -q generator:generate || exit 1
