#!/bin/sh

GLUE_DIR="java-gtk/build/resources/main/glue"

cat /etc/os-release
pwd

test -f gradlew || cd ..

VERSION="0.1"

pwd

if ! test -f build/pub-key.gpg; then
  mkdir -p build
  wget -nv -O - https://github.com/bailuk.gpg > build/pub-key
  gpg --dearmor build/pub-key
fi


download_arch () {
  if ! test -d ${GLUE_DIR}/${ARCH}; then
    mkdir -p ${GLUE_DIR}/${ARCH}
    wget -nv --no-check-certificate -P ${GLUE_DIR}/${ARCH}/ https://bailu.ch/java-gtk/v${VERSION}/${ARCH}/libglue.so.gpg
    gpg --keyring ./build/pub-key.gpg --no-default-keyring --verify ${GLUE_DIR}/${ARCH}/libglue.so.gpg || exit 1
    gpg --output ${GLUE_DIR}/${ARCH}/libglue.so --decrypt ${GLUE_DIR}/${ARCH}/libglue.so.gpg || exit 1
    rm ${GLUE_DIR}/${ARCH}/libglue.so.gpg
  fi
}

ARCH="linux-aarch64"
download_arch

ARCH="linux-x86_64"
download_arch

find -name "libglue*"
