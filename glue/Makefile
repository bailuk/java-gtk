VERSION=SNAPSHOT

ARCH != uname -m
ARCH_JAVA != uname -m

ifeq ($(ARCH),x86_64)
	ARCH_JAVA=amd64
else ifeq ($(ARCH),aarch64)
	ARCH_JAVA=arm64
endif

SRC_GEN != find build/generated/src/main/c -name '*.c'
SRC_DST != find src/main/c -name '*.c'

SRCS = $(SRC_DST) $(SRC_GEN)
OBJECTS := $(SRCS:%.c=%.o)
OBJS := $(notdir $(OBJECTS))
OBJDIR=build/obj

ifndef JAVA_HOME
    # https://stackoverflow.com/questions/1117398/java-home-directory-in-linux
    JAVA_HOME = $$(dirname $$(dirname $$(readlink -f $$(which javac))))
endif

GTK_LIBS    != pkg-config --libs gtk4
GTK_CFLAGS  != pkg-config --cflags gtk4
INCLUDE_DIRS = -I../java-gtk/build/generated/sources/headers/java/main/ -I$(JAVA_HOME)/include/ -I$(JAVA_HOME)/include/linux/

LIBDIR=build/lib/glue/linux-$(ARCH)


lib_res_dir = build/lib/glue/linux-$(ARCH)
lib_res = $(lib_res_dir)/libglue.so

lib_share_dir = build
lib_share = $(lib_share_dir)/libjava-gtk-$(VERSION).so

vpath %.c $(sort $(dir $(SRCS)))


all : $(lib_res)


$(lib_res) : $(lib_share) | $(lib_res_dir)
	cp $(lib_share) $(lib_res)


$(lib_share) : $(addprefix $(OBJDIR)/, $(OBJS)) | $(lib_share_dir)
	cc -shared -Wl,-soname,libjava-gtk.so -o $@ $^ $(GTK_LIBS)


$(OBJDIR)/%.o : %.c | $(OBJDIR)
	cc $(INCLUDE_DIRS) $(GTK_CFLAGS) -fPIC -O3 -DPIC64 -c $< -o $@


$(OBJDIR):
	mkdir -p $@


$(lib_res_dir):
	mkdir -p $@


$(lib_share_dir):
	mkdir -p $@


clean:
	rm -rf build
