#!/bin/sh

# copy libglue.so from remote host to projects library resource path
# usage: ./copy-glue-from-remote.sh host

test -f gradlew || cd ..

host=$1
arch=`ssh ${host} uname -m`

echo "copy from ${host} (${arch})"
basepath="${host}:git/java-gtk"

libpath="glue/build/lib/glue/linux-${arch}"
libglue="${libpath}/libglue.so"

test -d ${libpath} || mkdir -p ${libpath} || exit 1
scp "${basepath}/${libglue}" ${libglue}   || exit 1

find ${libpath} -name *.so

