#!/bin/sh

GLUE_DIR="java-gtk/build/resources/main/glue"

test -f gradlew || cd ..

VERSION="0.1"

download_arch () {
  mkdir -p ${GLUE_DIR}/${ARCH}
  wget -P ${GLUE_DIR}/${ARCH}/ https://bailu.ch/java-gtk/v${VERSION}/${ARCH}/libglue.so
  wget -P ${GLUE_DIR}/${ARCH}/ https://bailu.ch/java-gtk/v${VERSION}/${ARCH}/libglue.so.gpg
}

ARCH="linux-aarch64"
download_arch

ARCH="linux-x86_64"
download_arch
