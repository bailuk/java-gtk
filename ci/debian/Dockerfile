# Creates an image containing everything to build this project
# Based on https://hub.docker.com/_/debian/

# podman build -t build-on-debian .
# podman run -it --name build-on-debian build-on-debian
# -> ./clone-and-build.sh


FROM docker.io/debian:bookworm
LABEL version="0.6.0"

ENV JAVA_TOOL_OPTIONS "-Dfile.encoding=UTF8"

COPY clone-and-build.sh clone-and-build.sh
RUN chmod +x clone-and-build.sh

RUN apt-get -y update
RUN apt-get -y upgrade
RUN apt-get -y install libgtk-4-1
RUN apt-get -y install openjdk-11-jdk
RUN apt-get -y install libgirepository1.0-dev
RUN apt-get -y install build-essential
RUN apt-get -y install libadwaita-1-0
RUN apt-get -y install libadwaita-1-dev
RUN apt-get -y install libgtk-4-dev
RUN apt-get -y install libgeoclue-2-dev
RUN apt-get -y clean
