# Creates an image containing everything to build this project
# Based on https://hub.docker.com/_/debian/

# podman build -t build-on-debian .
# podman run -it --name build-on-debian build-on-debian
# -> ./clone-and-build.sh


FROM docker.io/debian:trixie
LABEL version="0.7.0"

ENV JAVA_TOOL_OPTIONS "-Dfile.encoding=UTF-8"

COPY clone-and-build.sh clone-and-build.sh
RUN chmod +x clone-and-build.sh

RUN apt-get -y update
RUN apt-get -y upgrade
RUN apt-get -y install git
RUN apt-get -y install openjdk-21-jdk-headless
RUN apt-get -y install libgtk-4-1
RUN apt-get -y install libadwaita-1-0
RUN apt-get -y clean
