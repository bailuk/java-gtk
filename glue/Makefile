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
    # https://stackoverflow.com/questions/1117398/java-home-directory-in-linux
    JAVA_HOME = $$(dirname $$(dirname $$(readlink -f $$(which javac))))
endif

GTK_LIBS    != pkg-config --libs gtk4
GTK_CFLAGS  != pkg-config --cflags gtk4
INCLUDE_DIRS = -I../library/build/generated/sources/headers/java/main/ -I$(JAVA_HOME)/include/ -I$(JAVA_HOME)/include/linux/

LIBDIR=build/lib/glue/linux-$(ARCH)
PRODUCT=$(LIBDIR)/libglue.so

vpath %.c $(sort $(dir $(SRCS)))

all : $(PRODUCT)

$(PRODUCT) : $(addprefix $(OBJDIR)/, $(OBJS)) | $(LIBDIR)
	cc -shared -Wl,-soname,libglue.so -o $@ $^ $(GTK_LIBS)


$(OBJDIR)/%.o : %.c | $(OBJDIR)
	cc $(INCLUDE_DIRS) $(GTK_CFLAGS) -fPIC -O3 -DPIC64 -c $< -o $@


$(OBJDIR):
	mkdir -p $@


$(LIBDIR):
	mkdir -p $@


clean:
	rm $(OBJDIR)/*.o
	rm $(PRODUCT)
