plugins {
    application
}


dependencies {
    implementation(project(":library"))

    val soDir = project(":glue").dependencyProject.buildDir
    fileTree("${soDir}/lib/main/release/linux.x86-64") {
        include("*.so*")
    }
}


application {
    mainClass.set("examples.HelloWorld")
}

