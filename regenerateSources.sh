#!/bin/sh

# clean library module and glue module
# compile and run source generator

echo "Clean all"
./gradlew library:clean || exit 1
rm -r glue/build

echo "Generate sources"
./gradlew generator:generate || exit 1

echo "Generate JNI headers"
./gradlew library:classes || exit 1