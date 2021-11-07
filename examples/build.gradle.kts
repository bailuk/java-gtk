plugins {
    application
}
repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":library"))
}

application {
    val example = "examples.App"
    mainClass.set(example)
}

