/**
 * run 'gradle wrapper --gradle-version 7.3.3 --distribution-type all' to init
 */
plugins {
    application
}
repositories {
    mavenCentral()
    maven {
        url = uri("file:////usr/share/maven-repo")
    }
}

dependencies {
    implementation("ch.bailu.java-gtk:java-gtk:0.1")
}

application {
    mainClass.set("com.example.App")
}
