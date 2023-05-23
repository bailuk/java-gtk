plugins {
    /**
     * https://github.com/allegro/axion-release-plugin
     *
     * 1. Current build version
     * ./gradlew cV
     *
     * 2. Code freeze
     * git tag v0.4.0-rc.1 -m "Code freeze"
     * git push origin v0.4.0-rc.1
     *
     * 3. Release
     * -> Update README.md
     * git tag v0.4.0 -m "Release"
     * git push origin v0.4.0
     */
    id ("pl.allegro.tech.build.axion-release") version "1.15.0"
}

project.version = scmVersion.version
