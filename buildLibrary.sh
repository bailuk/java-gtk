#!/bin/sh

# compile glue and build library

buildLog="glue/build/build.log"

echo "\nCompile C code. See '${buildLog}' for details"
if ! make -C glue -j $1 > ${buildLog} 2>&1; then
  tail ${buildLog}
  exit 1
fi
find glue/ -name *.so

echo "\nBuild java archives"
./gradlew -q library:build || exit 1
find library/ -name *.jar
