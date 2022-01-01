#!/bin/sh

# compile glue and build library

buildLog="glue/build/build.log"

if [ -n "$1" ]; then
    prozesses=$1
else
    prozesses=3
fi

echo
echo "Compile C code. See '${buildLog}' for details"
if ! make -C glue -j${prozesses} > ${buildLog} 2>&1; then
  tail ${buildLog}
  exit 1
fi
find glue/ -name *.so

echo
echo "Build java archives"
./gradlew -q library:build || exit 1
find library/ -name *.jar
