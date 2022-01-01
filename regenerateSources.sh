#!/bin/sh

# clean library module and glue module
# compile and run source generator

echo
echo "Clean all"
./gradlew -q library:clean || exit 1
test -d glue/build && rm -r glue/build

echo
echo "Generate sources"
./gradlew -q generator:generate || exit 1

echo
echo "Generate JNI headers"
./gradlew -q library:classes || exit 1
