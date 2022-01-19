#!/bin/sh

# copy ci/debian/build/*.deb from remote host to project
# usage: ./copy-deb-from-remote.sh host

test -f gradlew || cd ..

host=$1
arch=`ssh ${host} uname -m`

echo "copy from ${host} (${arch})"

basepath="${host}:git/java-gtk"
build="ci/debian/build/"

scp "${basepath}/${build}*.deb" ${build} || exit 1
find ${build} -name *.deb
