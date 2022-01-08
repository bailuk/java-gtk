
export LANG=C
export GRADLE_USER_HOME=$(HOME)/.gradle
export VERSION

orig_archive = build/java-gtk_$(VERSION).orig.tar.gz
build_dir = build/java-gtk-$(VERSION)
build_dir_debian = build/java-gtk-$(VERSION)/debian

all: src

src : $(build_dir_debian)
	cd $(build_dir) && debuild -e LANG -e GRADLE_USER_HOME -e VERSION -us -uc -S '-I\.gradle'

bin: $(build_dir_debian)
	debuild -us -uc -debuild

$(build_dir_debian) : $(build_dir)
	cp -rv debian $(build_dir)


$(build_dir): $(orig_archive)
	cd build && tar -xzvf java-gtk_$(VERSION).orig.tar.gz

$(orig_archive):
	- mkdir build
	cd ../../ && git archive --prefix=java-gtk-$(VERSION)/ --format=tar.gz  HEAD -o $(CURDIR)/$@

clean:
	- rm -rf build

FORCE:
