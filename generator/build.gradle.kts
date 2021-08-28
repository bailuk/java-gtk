
plugins {
    groovy
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")

    /**
     *  https://mvnrepository.com/artifact/net.sf.kxml/kxml2
     *  xml parser implementation
     */
    implementation("net.sf.kxml:kxml2:2.3.0")

    // https://mvnrepository.com/artifact/org.codehaus.groovy/groovy-all
    implementation("org.codehaus.groovy:groovy-all:3.0.8")

}

tasks.test {
    useJUnitPlatform()
}


tasks.register("generate", JavaExec::class) {
    dependsOn("build")
    setDescription("Generate source code from introspective files")
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("ch.bailu.gtk.App")

    args(setOf(
            "-i", "/usr/share/gir-1.0",
            "-j", "${project.getRootDir()}/library/build/generated/src/main/java/ch/bailu/gtk/",
            "-c", "${project.getRootDir()}/glue/build/generated/src/main/c/"))
}

