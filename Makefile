# This project uses GNU Make as its main build system.
# This is the top-level build file. It sticks to conventions expected by debuild.
# It will call ./gradlew for java related builds.
#
# Usages:
#
#  - Build jar and install it to local maven repository:
#		make install
#
#  - Build generator and generate all sources (including jni headers)
#       make gen
#
#  - Build and test everything and then run the selected example
#       make run
#

ifndef VERSION
	VERSION=$(shell ./gradlew cV -q -Prelease.quiet)
endif

ifndef JOBS
	JOBS=9
endif

export JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF8

generator_jar = generator/build/libs/generator.jar
gen_source_marker = build/gen-source.marker
gen_header_marker = build/gen-header.marker

jlib = java-gtk/build/libs/java-gtk-$(VERSION).jar

ifdef DESTDIR
	# global
	install_target = install_global

	# DESTDIR path must be absolute, else gradle will ignore this
	m2_repo = $(DESTDIR)/usr/share/maven-repo

else
	# local
	install_target = install_local
	m2_repo = $(HOME)/.m2/repository
endif

m2_dir = $(m2_repo)/ch/bailu/java-gtk/

all: $(jlib)


install: $(install_target)


install_local: all uninstall
	./gradlew cV -q -Prelease.quiet
	./gradlew -q publishToMavenLocal -Dmaven.repo.local=$(m2_repo)


install_global: all uninstall
	./gradlew cV -q -Prelease.quiet
	./gradlew -q publishToMavenLocal -Dmaven.repo.local=$(m2_repo) -PjarType=shared

clean:
	./gradlew cV -q -Prelease.quiet
	./gradlew -q clean
	- rm -rf build

distclean: FORCE
	- rm -rf .gradle
	- rm -rf build
	- rm -rf java-gtk/build
	- rm -rf generator/build
	- rm -rf examples/build
	- rm -rf ci/debian/build

maintainer-clean: distclean
	echo "maintainer-clean"

uninstall:
	- rm -r $(m2_dir)

dist:
	make install DESTDIR=$(CURDIR)/build/java-gtk-$(VERSION)
	cd build && tar -czvf java-gtk-$(VERSION).tar.gz java-gtk-$(VERSION)

dist-nogen:
	echo "dist-nogen"

distcheck:
	echo "distcheck"

examples: $(jlib)
	./gradlew cV -q -Prelease.quiet
	./gradlew examples:build

run: $(jlib)
	./gradlew cV -q -Prelease.quiet
	./gradlew examples:run

gen: $(gen_source_marker) $(gen_header_marker)

jdoc: $(gen_source_marker)
	./gradlew cV -q -Prelease.quiet
	./gradlew -q java-gtk:javadocJar

jdoc-install: $(jdoc) javadoc
	rm -rf javadoc/*
	unzip -q java-gtk/build/libs/java-gtk-$(VERSION)-javadoc.jar -d javadoc

javadoc:
	mkdir javadoc


$(jlib): gen FORCE
	./gradlew cV -q -Prelease.quiet
	./gradlew -q java-gtk:build

$(gen_header_marker): $(gen_source_marker)
	./gradlew cV -q -Prelease.quiet
	./gradlew -q java-gtk:classes
	touch $(gen_header_marker)

$(gen_source_marker): $(generator_jar)
	./gradlew cV -q -Prelease.quiet
	./gradlew -q generator:generate
	touch $(gen_source_marker)

$(generator_jar): FORCE
	./gradlew cV -q -Prelease.quiet
	./gradlew -q generator:build

FORCE:
