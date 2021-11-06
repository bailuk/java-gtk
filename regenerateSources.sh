#!/bin/sh

# clean library module and glue module
# compile and run source generator

./gradlew library:clean
rm -r glue/build
./gradlew generator:generate
