plugins {
    `java-library`
    `maven-publish`
}

fun getProperty(property: String, default: String) : String {
    var result = default

    if (project.hasProperty(property)) {
        val r = project.property(property)
        if (r is String && r != "unspecified") {
            result = r
        }
    }
    return result
}

project.version = getProperty("version", "SNAPSHOT")
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

    implementation("com.github.jnr:jnr-ffi:2.2.11")
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")
}


tasks.test {
    val libraryPath = file("${project(":glue").buildDir}/lib/main/debug").absolutePath
    systemProperty( "java.library.path", libraryPath)
    useJUnitPlatform()
}


/** add generated code and C library to source set **/
sourceSets {
    main {
        java {
            val src = File(project(":java-gtk").buildDir,"generated/src/main/java")
            srcDir(src)
        }

        resources {
            val res = File("../glue/build/lib/")
            srcDir(res)
        }

    }
}


/** exclude C library from source jar **/
tasks.named("sourcesJar") {
    if (this is org.gradle.jvm.tasks.Jar) {
        exclude("/glue/")
    }
}


tasks.jar {
    // exclude C library from shared installation
    if (getProperty("jarType", "resource") == "shared") {
        exclude("/glue/*-*")
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

