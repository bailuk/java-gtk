#!/bin/sh

# clean library module and glue module
# compile and run source generator

echo "\nClean all"
./gradlew -q library:clean || exit 1
test -d glue/build && rm -r glue/build

echo "\nGenerate sources"
./gradlew -q generator:generate || exit 1

echo "\nGenerate JNI headers"
./gradlew -q library:classes || exit 1
