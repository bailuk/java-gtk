#!/bin/sh

# if javadoc directory exists
# create javadoc and extract javadoc to javadoc directory

if [ -d javadoc ]; then
    ./gradlew library:javadocJar && unzip library/build/libs/library-0.1.0-SNAPSHOT-javadoc.jar -d javadoc
fi
