#!/bin/sh

# use `make install` instead

test -f gradlew || cd ..

repoDir="${HOME}/.m2/repository/ch/bailu/java-gtk/"

echo
echo "Remove 'java-gtk' from local Maven repository"
rm -r ${repoDir}

echo
echo "Install 'java-gtk' to local Maven repository"
./gradlew -q publishToMavenLocal || exit 1
find ${repoDir}
