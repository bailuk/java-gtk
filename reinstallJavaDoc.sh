#!/bin/sh

# Removes directory 'javadoc/' and
# installs Java Doc to directory 'javadoc/'

echo "Clean Java Doc"
rm -r javadoc

echo "Generate Java Doc"
mkdir javadoc || exit 1
./gradlew library:javadocJar || exit 1
unzip library/build/libs/library-0.1.0-SNAPSHOT-javadoc.jar -d javadoc || exit 1

echo "Java Doc is in 'javadoc/'"