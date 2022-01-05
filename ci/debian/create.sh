#!/bin/sh

# https://wiki.debian.org/Packaging/Intro?action=show&redirect=IntroDebianPackaging
version="0.1"

mkdir build
cd ../../
git archive --prefix=java-gtk-${version}/ --format=tar.gz  HEAD -o ci/debian/build/java-gtk_${version}.orig.tar.gz
cd ci/debian/build
tar -xzvf java-gtk_${version}.orig.tar.gz
ln -s ../debian .

# export DEBEMAIL="bailu@bailu.ch"
# dch --create -v 0.1-1 --package inital

# compatibility level for dephelper tool
# echo 10 > debian/compat
