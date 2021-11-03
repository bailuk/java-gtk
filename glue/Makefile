machine != uname -m
machine_java != uname -m

ifeq ($(machine),x86_64)
	machine_java=amd64
else ifeq ($(machine),aarch64)
	machine_java=arm64
endif

source_dir_gen = build/generated/src/main/c
source_dir_dst = src/main/c
sources_gen != find $(source_dir_gen) -name '*.c'
sources_dst != find $(source_dir_dst) -name '*.c'

sources = $(sources_dist) $(sources_gen)
objects := $(sources:%.c=%.o)
objs := $(notdir $(objects))
obj_dir=build/obj



include_jni_dir = ../library/build/generated/sources/headers/java/main/
include_sys_dir = /usr/include/
include_sub_dirs = glib-2.0 gtk-3.0 pango-1.0 harfbuzz cairo gdk-pixbuf-2.0 atk-1.0 gio-unix-2.0
includes:=$(foreach dir,$(include_sub_dirs), -I$(include_sys_dir)$(dir))
includes+= -I$(include_jni_dir) -I/usr/lib/jvm/java-11-openjdk-$(machine_java)/include/ -I/usr/lib/jvm/java-11-openjdk-$(machine_java)/include/linux/ -I/usr/lib/$(machine)-linux-gnu/glib-2.0/include/

libraries = z gtk-3 gdk-3 pangocairo-1.0 pango-1.0 harfbuzz atk-1.0 cairo-gobject cairo gdk_pixbuf-2.0 gio-2.0 gobject-2.0 glib-2.0
libs:=$(foreach lib,$(libraries), -l$(lib))

product=build/lib/linux-${machine}/libglue.so

vpath %.c $(sort $(dir $(sources)))

all : $(product)

$(product) : $(addprefix $(obj_dir)/, $(objs))
	mkdir -p build/lib/linux-${machine}
	cc -shared -Wl,-soname,libglue.so -o $(product) $^ $(libs) -m64

$(obj_dir)/%.o : %.c
	cc -s -fPIC -O3 -DPIC64 -pthread $(includes) -c $< -o $@

clean:
	rm *.o
	rm *.so
