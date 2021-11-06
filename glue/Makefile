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

ifeq ($(JAVA_HOME),)
	JAVA_HOME = /usr/lib/jvm/java-11-openjdk-$(ARCH_JAVA)
endif

INCDIR_SYS = /usr/include/
INCDIRS_SYS = glib-2.0 gtk-3.0 pango-1.0 harfbuzz cairo gdk-pixbuf-2.0 atk-1.0 gio-unix-2.0
INCS:=$(foreach dir,$(INCDIRS_SYS), -I$(INCDIR_SYS)$(dir))
INCS+= -I../library/build/generated/sources/headers/java/main/ -I$(JAVA_HOME)/include/ -I$(JAVA_HOME)/include/linux/ -I/usr/lib/$(ARCH)-linux-gnu/glib-2.0/include/

libraries = z gtk-3 gdk-3 pangocairo-1.0 pango-1.0 harfbuzz atk-1.0 cairo-gobject cairo gdk_pixbuf-2.0 gio-2.0 gobject-2.0 glib-2.0
LIBS:=$(foreach lib,$(libraries), -l$(lib))

LIBDIR=build/lib/glue/linux-$(ARCH)
PRODUCT=$(LIBDIR)/libglue.so

vpath %.c $(sort $(dir $(SRCS)))

all : $(PRODUCT)

$(PRODUCT) : $(addprefix $(OBJDIR)/, $(OBJS)) | $(LIBDIR)
	cc -shared -Wl,-soname,libglue.so -o $@ $^ $(LIBS)


$(OBJDIR)/%.o : %.c | $(OBJDIR)
	cc -s -fPIC -O3 -DPIC64 -pthread $(INCS) -c $< -o $@


$(OBJDIR):
	mkdir -p $@


$(LIBDIR):
	mkdir -p $@


clean:
	rm $(OBJDIR)/*.o
	rm $(PRODUCT)
