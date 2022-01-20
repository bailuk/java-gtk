#!/bin/sh


cat /etc/os-release
pwd
ls -lh

echo; echo

test -f gradlew || cd ..

VERSION="0.1"
GLUE="glue/build/lib/glue"
KEY="build/pub-key.gpg"

if ! test -f ${KEY}; then
  mkdir build
  wget -nv -O - https://github.com/bailuk.gpg | gpg --dearmor -o ${KEY}
fi

download_arch () {
  ARCH=$1
  LIB="${GLUE}/${ARCH}/libglue.so"
  URL="https://bailu.ch/java-gtk/v${VERSION}/${ARCH}/libglue.so.gpg"
  
  if ! test -f ${LIB}; then
    mkdir -p ${GLUE}/${ARCH}
    wget -nv --no-check-certificate -O - ${URL} | gpg --keyring ./${KEY} --no-default-keyring -o ${LIB} --decrypt
  fi
}

download_arch "linux-aarch64"
download_arch "linux-x86_64"

find -name "libglue*"

echo; echo
