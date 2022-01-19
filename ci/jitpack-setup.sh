#!/bin/sh


cat /etc/os-release
pwd
ls -lh

echo; echo

test -f gradlew || cd ..

VERSION="0.1"
GLUE="java-gtk/build/resources/main/glue"
KEY="build/pub-key.gpg"

if ! test -f ${KEY}; then
  mkdir build
  wget -nv -O - https://github.com/bailuk.gpg | gpg --dearmor -o ${KEY}
fi

download_arch () {
  LIB="${GLUE}/${ARCH}/libglue.so"
  URL="https://bailu.ch/java-gtk/v${VERSION}/${ARCH}/libglue.so.gpg"
  
  if ! test -f ${LIB}; then
    mkdir -p ${GLUE}/${ARCH}
    wget -nv --no-check-certificate -O - ${URL} | gpg --keyring ./${KEY} --no-default-keyring -o ${LIB} --decrypt
  fi
}

ARCH="linux-aarch64"
download_arch

ARCH="linux-x86_64"
download_arch

find -name "libglue*"

echo; echo
