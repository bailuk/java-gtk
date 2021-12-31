# Creates an image containing everything to build the gtk4 branch of this project
# Based on https://hub.docker.com/_/debian/
FROM docker.io/debian:bookworm
LABEL version="0.2"

ENV JAVA_TOOL_OPTIONS "-Dfile.encoding=UTF8"

COPY clone-and-build.sh clone-and-build.sh
RUN chmod +x clone-and-build.sh

RUN apt-get -y update && \
    apt-get -y upgrade && \
    apt-get -y install libgtk-4-dev openjdk-11-jdk libgirepository1.0-dev git
