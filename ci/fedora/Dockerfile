# A fedora based build image
# Based on https://hub.docker.com/_/fedora/

# podman build -f Dockerfile -t build-on-fedora
# podman run -it --name build-on-fedora build-on-fedora:latest
# => ./clone-and-build.sh

FROM docker.io/fedora:36
LABEL version="0.4.0"

ENV JAVA_TOOL_OPTIONS "-Dfile.encoding=UTF8"

COPY clone-and-build.sh clone-and-build.sh
RUN chmod +x clone-and-build.sh

RUN dnf -y upgrade
RUN dnf -y install gtk4
RUN dnf -y install java-11-openjdk
RUN dnf -y install java-11-openjdk-devel
RUN dnf -y install git
RUN dnf -y install make
RUN dnf -y install libadwaita
RUN dnf -y install gtk4-devel
RUN dnf -y clean all
