#!/bin/sh

# assemble library
# compile glue
# build library

echo "Compile C code. See 'build/build.log' for details"
cd glue || exit 1
make -j > build/build.log 2>&1 || exit 1
cd ..

echo "Build java archives"
./gradlew library:build
find library/ -name *.jar
