JOBS=9
VERSION=SNAPSHOT

generator_jar = generator/build/libs/generator.jar
gen_source_marker = build/gen-source.marker
gen_header_marker = build/gen-header.marker

clib_shared=glue/build/lib/libjava-gtk.so.$(VERSION)

jlib_fat = java-gtk/build/libs/java-gtk-$(VERSION).jar

m2_dir = $(HOME)/.m2/repository/ch/bailu/java-gtk/


all: $(clib_shared) $(jlib_fat)


install: all uninstall
	./gradlew -q publishToMavenLocal

clean:
	./gradlew -q clean
	make -C glue clean
	rm -rf build

distclean: clean
	echo "distclean"

maintainer-clean: distclean
	echo "maintainer-clean"

uninstall:
	rm -r $(m2_dir)

dist:
	echo "dist"

dist-nogen:
	echo "dist-nogen"

distcheck:
	echo "distcheck"

clib: $(clib_shared)

run: $(jlib_fat)
	./gradlew examples:run

$(jlib_fat): $(clib_shared) FORCE
	./gradlew -q java-gtk:build -PVERSION=$(VERSION)

$(clib_shared): $(gen_source_marker) $(gen_header_marker)
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
