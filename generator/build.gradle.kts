
plugins {
    java

    // https://kotlinlang.org/docs/gradle-configure-project.html
    kotlin("jvm") version "1.9.0"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")

    // https://mvnrepository.com/artifact/net.sf.kxml/kxml2
    // xml parser implementation
    implementation("net.sf.kxml:kxml2:2.3.0")

}

tasks.test {
    useJUnitPlatform()
}


tasks.register("generate", JavaExec::class) {
    dependsOn("build")
    description = "Generate source code from introspective files"
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("ch.bailu.gtk.AppKt")

    args(setOf("-j", "${project.rootDir}/java-gtk/build/generated/src/main/java/ch/bailu/gtk/"))
}

tasks.compileJava {
    options.compilerArgs.add("-Xlint:deprecation")
    options.compilerArgs.add("-Xlint:unchecked")
}
