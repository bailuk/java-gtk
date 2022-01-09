# Debian related info
## Build container
Use `Dockerfile` to create a Debian based build container for this project.

## Build packages
1. Remove `SNAPSHOT` from version in _root_ Makefile.
2. Execute `make deb` in this projects _root_ directory to build Debian packages.
Configuration files are in `ci/debian/debian`

### Tools
- debchange (alter debian/changelog): `dch --create -v 0.1-1 --package inital`
