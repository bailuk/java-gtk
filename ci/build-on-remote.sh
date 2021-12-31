#!/bin/sh

# build this project on a remote device via ssh
# usage: ./build-on-remote.sh host

host=$1
arch=`ssh ${host} uname -m`

echo "build on ${host} for ${arch}"

ssh ${host} "test -d git || mkdir git"         || exit 1
scp clone-and-build.sh mobian:git              || exit 1
ssh ${host} "chmod +x git/clone-and-build.sh"  || exit 1
ssh ${host} "cd git && ./clone-and-build.sh 3" || exit 1
