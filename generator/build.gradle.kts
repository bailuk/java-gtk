
plugins {
    java
    
    // https://kotlinlang.org/docs/gradle.html#targeting-the-jvm
    kotlin("jvm") version "1.6.10"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")

    /**
     *  https://mvnrepository.com/artifact/net.sf.kxml/kxml2
     *  xml parser implementation
     */
    implementation("net.sf.kxml:kxml2:2.3.0")

}


tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions { jvmTarget = "11" }
}


tasks.test {
    useJUnitPlatform()
}


tasks.register("generate", JavaExec::class) {
    dependsOn("build")
    setDescription("Generate source code from introspective files")
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("ch.bailu.gtk.AppKt")

    args(setOf(
            "-i", "/usr/share/gir-1.0",
            "-j", "${project.getRootDir()}/java-gtk/build/generated/src/main/java/ch/bailu/gtk/"))
}

tasks.compileJava {
    options.compilerArgs.add("-Xlint:deprecation")
    options.compilerArgs.add("-Xlint:unchecked")
}