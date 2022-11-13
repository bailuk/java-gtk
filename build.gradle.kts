plugins {
    /**
     * https://github.com/allegro/axion-release-plugin
     *
     * 1. Current build version
     * ./gradlew cV
     *
     * 2. Move to next version
     * tag v0.3.0-alpha -m "Move to next version"
     *
     * 3. Release
     * -> Update README.md
     * tag v0.3.0 "Release"
     */
    id ("pl.allegro.tech.build.axion-release") version "1.14.0"
}

project.version = scmVersion.version
