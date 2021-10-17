#!/bin/sh

if [ -d javadoc ]; then
    ./gradlew build
    unzip library/build/libs/library-0.1.0-SNAPSHOT-javadoc.jar -d javadoc
fi

