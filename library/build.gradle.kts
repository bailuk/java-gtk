plugins {
    `java-library`
    `maven-publish`
}

fun getProperty(property: String, default: String) : String {
    var result = default

    if (project.hasProperty(property)) {
        val r = project.property(property)
        if (r is String) {
            result = r
        }
    }
    return result
}

project.version = getProperty("VERSION", "SNAPSHOT")
project.group = "ch.bailu.java-gtk"

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/bailuk/java-gtk")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")

            }

        }
    }
    publications {
        register<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
}


repositories {
    mavenCentral()
}


dependencies {
    // implementation of javax.annotation:javax.annotation-api
    // https://mvnrepository.com/artifact/com.google.code.findbugs/jsr305
    api("com.google.code.findbugs:jsr305:3.0.2")

    testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")
}


tasks.test {
    val libraryPath = file("${project(":glue").buildDir}/lib/main/debug").absolutePath
    systemProperty( "java.library.path", libraryPath)
    useJUnitPlatform()
}

sourceSets {
    main {
        java {
            val src = File(project(":library").buildDir,"generated/src/main/java")
            srcDir(src)
        }

        resources {
            val res = File( "../glue/build/lib/")
            srcDir(res)
        }
    }
}

tasks.compileJava {
    if (this is JavaCompile) {
        options.compilerArgs.add("-Xlint:deprecation")
        options.compilerArgs.add("-Xlint:unchecked")
    }
}

tasks.javadoc {
    /*
        https://docs.gradle.org/current/javadoc/org/gradle/external/javadoc/StandardJavadocDocletOptions.html
    */
    options.windowTitle("java-gtk")
    options.overview("java-gtk")
    (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
    (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    (options as StandardJavadocDocletOptions).footer = "Test"
}

