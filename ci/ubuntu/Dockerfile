# A Ubuntu based build image
#
# This should be as identical as possible with the official GitHub Actions Ubuntu image
#     https://github.com/actions/runner-images
#
# Based on https://hub.docker.com/_/ubuntu/

# podman build -f Dockerfile -t build-on-ubuntu
# podman run -it --name build-on-ubuntu build-on-ubuntu:latest
# => ./clone-and-build.sh

FROM docker.io/ubuntu:latest
LABEL version="0.1.0"

ENV JAVA_TOOL_OPTIONS "-Dfile.encoding=UTF8"

COPY clone-and-build.sh clone-and-build.sh
RUN chmod +x clone-and-build.sh

RUN apt-get -y update
RUN apt-get -y upgrade
RUN apt-get -y install git
RUN apt-get -y install openjdk-11-jdk-headless
RUN apt-get -y install libgtk-4-1
RUN apt-get -y clean
