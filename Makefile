# This project uses GNU Make as its main build system.
# This is the top-level build file. It sticks to conventions expected by debuild.
# It will call ./gradlew for java related builds and make -C glue to build C code.
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
	VERSION=SNAPSHOT
endif

ifndef JOBS
	JOBS=9
endif

export JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF8

generator_jar = generator/build/libs/generator.jar
gen_source_marker = build/gen-source.marker
gen_header_marker = build/gen-header.marker

clib = glue/build/libjava-gtk-$(VERSION).so
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
clib_target = $(DESTDIR)/usr/lib/jni

all: $(clib) $(jlib)


install: $(install_target)


install_local: all uninstall
	./gradlew -q publishToMavenLocal -Dmaven.repo.local=$(m2_repo) -Pversion=$(VERSION)


install_global: all uninstall
	./gradlew -q publishToMavenLocal -Dmaven.repo.local=$(m2_repo) -Pversion=$(VERSION)
	mkdir -p $(clib_target)
	cp $(clib) $(clib_target)/


clean:
	./gradlew -q clean
	make -C glue clean
	- rm -rf build

distclean: clean
	- rm -rf .gradle

maintainer-clean: distclean
	echo "maintainer-clean"

uninstall:
	- rm -r $(m2_dir)

dist:
	echo "dist"

dist-nogen:
	echo "dist-nogen"

distcheck:
	echo "distcheck"

clib: $(clib)

run: $(jlib)
	./gradlew examples:run

gen: $(gen_source_marker) $(gen_header_marker)

deb: FORCE
	make -C ci/debian VERSION=$(VERSION)

deb-clean:
	make -C ci/debian clean

$(jlib): $(clib) FORCE
	./gradlew -q java-gtk:build -Pversion=$(VERSION)

$(clib): $(gen_source_marker) $(gen_header_marker)
	make -j $(JOBS) -C glue VERSION=$(VERSION)

$(gen_header_marker): $(gen_source_marker)
	./gradlew -q java-gtk:classes
	touch $(gen_header_marker)

$(gen_source_marker): $(generator_jar)
	./gradlew -q generator:generate
	touch $(gen_source_marker)

$(generator_jar): FORCE
	./gradlew -q generator:build

FORCE:
