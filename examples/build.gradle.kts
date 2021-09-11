plugins {
    application
}

dependencies {
    implementation(project(":library"))
}

application {
    val example = "examples.App"
    mainClass.set(example)
}

tasks.withType(JavaExec::class.java) {
    val libraryPath = file("${project(":glue").buildDir}/lib/main/debug").absolutePath
    systemProperty( "java.library.path", libraryPath)
    dependsOn( ":glue:linkDebug")
}

