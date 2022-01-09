#!/bin/sh

# use `make` instead
#
# compile glue and build library

test -f gradlew || cd ..


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
./gradlew -q java-gtk:build || exit 1
find java-gtk/ -name *.jar
