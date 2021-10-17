plugins {
    `java-library`
    `maven-publish`
    id("pl.allegro.tech.build.axion-release") version "1.13.3"
}

project.version = scmVersion.version
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
    //implementation("org.jetbrains:annotations:15.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")
}


tasks.test {
    val libraryPath = file("${project(":glue").buildDir}/lib/main/debug").absolutePath
    systemProperty( "java.library.path", libraryPath)
    dependsOn( ":glue:linkDebug")
    useJUnitPlatform()
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

tasks.processResources {
    dependsOn(":glue:linkRelease")
}


tasks.compileJava {
    if (this is JavaCompile) {
        options.compilerArgs.add("-Xlint:deprecation")
        options.compilerArgs.add("-Xlint:unchecked")
    }
    dependsOn(":generator:generate")
}

tasks.javadoc {
    /*
        https://docs.gradle.org/current/javadoc/org/gradle/external/javadoc/StandardJavadocDocletOptions.html
    */
    options.windowTitle("java-gtk")
    options.overview("java-gtk")
    (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    (options as StandardJavadocDocletOptions).footer = "Test"
}

tasks.named("sourcesJar") {
    dependsOn(":glue:linkRelease")
}
/*
tasks {
    val sourcesJar by creating(Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets.main.get().allJava)
    }

    val javadocJar by creating(Jar::class) {
        dependsOn.add(javadoc)
        archiveClassifier.set("javadoc")
        from(javadoc)
    }

    artifacts {
        archives(sourcesJar)
        archives(javadocJar)
        archives(jar)
    }
}

*/
