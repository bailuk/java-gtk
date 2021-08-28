
plugins {
    id 'java-library'
    id 'maven-publish'
}

group = 'ch.bailu.java-gtk'
version = '0.1'

publishing {
    publications {
        mavenJava(MavenPublication) {
            artifactId = 'java-gtk'
            from components.java
            versionMapping {
                usage('java-api') {
                    fromResolutionOf('runtimeClasspath')
                }
                usage('java-runtime') {
                    fromResolutionResult()
                }

            }
            pom {
                name = 'java-gtk'
                description = 'Java bindings for GTK'
            }
        }

    }
}


repositories {
    mavenCentral()
}


dependencies {
    testImplementation 'org.junit.jupiter:junit-jupiter:5.7.1'
}


test {
    dependsOn  ':glue:linkDebug'
    useJUnitPlatform()
    systemProperty "java.library.path", file("${project(":glue").buildDir}/lib/main/debug").absolutePath

}


sourceSets {
    main {
        java.srcDirs += 'build/generated/src/main/java'
        resources.srcDirs += "${project(":glue").buildDir}/lib/main/release"
    }
}

jar {
    dependsOn ':glue:linkRelease'
}


compileJava {
    dependsOn ':generator:generate'
}

