plugins {
    `java-library`
    `maven-publish`
}
/*
group = "ch.bailu.java-gtk"
version = "0.1"

// https://docs.gradle.org/current/userguide/publishing_maven.html
publishing {
    publications {
        create<MavenPublication>("java-gtk") {
            artifactId = "java-gtk"
            from(components["java"])
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
            pom {
                name.set("java-gtk")
                description.set("Java bindings for GTK")
            }
        }

    }
}*/

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "ch.bailu.java-gtk"
            artifactId = "java-gtk"
            version = "0.1"

            from(components["java"])
        }
    }
}


repositories {
    mavenCentral()
}


dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")
}


tasks.test {
    val libraryPath = file("${project(":glue").buildDir}/lib/main/debug").absolutePath

    dependsOn( ":glue:linkDebug")
    useJUnitPlatform()
    systemProperty( "java.library.path", libraryPath)
}



sourceSets {
    main {
        java {
            val src = File(project(":library").buildDir,"generated/src/main/java")
            srcDir(src)
        }

        resources {
            compiledBy(":glue:linkRelease")
            val res = File(project(":glue").buildDir, "lib/main/release")
            srcDir(res)
        }
    }
}

tasks.named("processResources") {
    dependsOn(":glue:linkRelease")
}


tasks.compileJava {
    if (this is JavaCompile) {
        options.compilerArgs.add("-Xlint:deprecation")
        options.compilerArgs.add("-Xlint:unchecked")
    }
    dependsOn(":generator:generate")
}

