plugins {
    application
}
repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":java-gtk"))
}

application {
    val example = "examples.App"
    mainClass.set(example)
}
