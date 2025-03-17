plugins {
    /**
     * https://github.com/allegro/axion-release-plugin
     *
     * Current build version
     * ./gradlew cV
     *
     */
    id ("pl.allegro.tech.build.axion-release") version "1.18.18"
}

project.version = scmVersion.version
