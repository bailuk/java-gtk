#!/bin/sh

# assemble library
# compile glue
# build library

./gradlew library:assemble && cd glue && make -j && cd .. && ./gradlew library:build
