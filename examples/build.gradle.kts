plugins {
    application
}

dependencies {
    implementation(project(":library"))

    val glueBuild = project(":glue").dependencyProject.buildDir
    val libDir = "${glueBuild}/lib/main/debug/"
    implementation(fileTree(libDir) {include("*.so*")})
}

application {
    val example = "examples.HelloWorld"
    mainClass.set(example)
}

