plugins {
    application
}
repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":java-gtk"))
    implementation("net.java.dev.jna:jna:5.10.0")

}

application {
    val example = "examples.App"
    mainClass.set(example)
}

