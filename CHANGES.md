# Next Release (0.4.0)

- Generator: Improve record support (structure and callbacks)
- Generator: Java doc for callback class
- Generator: Add asInterfaceName() function to simplify casting
- Generator: Java doc: generate package-info.java
- Generator: Port unittests to kotlin
- Library: Add GSK and graphene API
- Library: InputStreamBridge
- Library: Support subclassing
- Library: [#9] Configurable library names in library Loader
- Examples: Port Adwaita demo (subclassing)
- CI: Update GIR Files (add script) and prefer from Project
- CI: Update dependencies: gradle:8.1, kotlin:1.8.20, jna:5.13.0, junit-jupiter:5.9.2, axion-release:1.15.0

# Release (0.3.0)

- Generator: Add java doc to signal connectors
- Generator: Support GeoClue2
- Generator: Varargs
- Generator: Native String (overload)
- Generator: Native Boolean
- Library: Add class handler
- Library: Add callback and signal handler
- Library: Add action handler
- Library: Reorganize package name space
- Library: "Dump resources" support for Handlers
- Library: Add util classes: CSS and UiBuilder
- Examples: Add GeoClue2 demo (where-am-i)
- Examples: Add CSS Accordion
- Examples: Put most demos into one example app
- Examples: Extend Huge List and remove threaded callback sample
- Examples: Port Adwaita demo: https://github.com/Northshore-Hero/libadwaita-demo
- CI: Update gradle: ./gradlew wrapper --gradle-version 7.5.1
- CI: Update JNA and libraries
- CI: Move to strict semantic versioning, add axion-release-plugin
- CI: remove deb support / update fedora and debian build container
