
plugins {
    java

    // https://kotlinlang.org/docs/gradle-configure-project.html
    kotlin("jvm") version "1.9.22"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")

    // https://mvnrepository.com/artifact/net.sf.kxml/kxml2
    // xml parser implementation
    implementation("net.sf.kxml:kxml2:2.3.0")

}

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            useJUnitJupiter()
        }
    }
}

tasks.register("generate-update-doc", JavaExec::class) {
    description = "Generate source code from introspective files and update meta documentation"
    registerGeneratorTask(this)
    args(setOf("-l", "../doc/gen"))
}

tasks.register("generate", JavaExec::class) {
    description = "Generate source code from introspective files"
    registerGeneratorTask(this)
}


fun registerGeneratorTask(task: JavaExec) {
    task.dependsOn("build")
    description = "Generate source code from introspective files"
    task.classpath = sourceSets["main"].runtimeClasspath
    task.mainClass.set("ch.bailu.gtk.AppKt")
    task.args(setOf("-j", "${project.rootDir}/java-gtk/build/generated/src/main/java/ch/bailu/gtk/"))
}

tasks.compileJava {
    options.compilerArgs.add("-Xlint:deprecation")
    options.compilerArgs.add("-Xlint:unchecked")
}
