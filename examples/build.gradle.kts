plugins {
    application
}
repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":java-gtk"))
    implementation("com.github.jnr:jnr-ffi:2.2.11")

}

application {
    val example = "examples.App"
    mainClass.set(example)
}

