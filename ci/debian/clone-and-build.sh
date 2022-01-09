#!/bin/sh

test -d java-gtk || git clone "https://github.com/bailuk/java-gtk.git" || exit 1

cd java-gtk  || exit 1
git pull     || exit 1

make install examples
