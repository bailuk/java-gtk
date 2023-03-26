plugins {
    `java-library`
    `maven-publish`
}

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

    // https://github.com/java-native-access/jna
    // https://mvnrepository.com/artifact/net.java.dev.jna/jna
    api("net.java.dev.jna:jna:5.12.1")

    //https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
}


tasks.test {
    useJUnitPlatform()
}


/** add generated code and C library to source set **/
sourceSets {
    main {
        java {
            val src = File(project(":java-gtk").buildDir,"generated/src/main/java")
            srcDir(src)
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
    options.overview("../doc/overview.html")
    (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
    (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
}
