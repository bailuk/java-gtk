#!/bin/sh

repoDir="${HOME}/.m2/repository/ch/bailu/java-gtk/"

echo "\nRemove 'java-gtk' from local Maven repository"
rm -r ${repoDir}

echo "\nInstall 'java-gtk' to local Maven repository"
./gradlew -q publishToMavenLocal || exit 1
find ${repoDir}
