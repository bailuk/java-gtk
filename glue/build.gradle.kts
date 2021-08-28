import org.gradle.internal.jvm.Jvm

/*
 * There is currently no "C application" plugin, so this build uses the "C++ application" plugin and then reconfigures it
 * to build C instead.
 *
 * From:
 * https://github.com/vladsoroka/GradleJniSample
 */
plugins {
    `cpp-library`
}

dependencies {
    val jniHeaders = "${project(":library").dependencyProject.buildDir}/generated/sources/headers/java/main"

/*
    implementation(files(jniHeaders) {
        builtBy(":library:compileJava")
    })
*/
}


val libraries = listOf(
            "/usr/lib/x86_64-linux-gnu/libgtk-3.so",
            "/usr/lib/x86_64-linux-gnu/libgdk-3.so",
            "/usr/lib/x86_64-linux-gnu/libpangocairo-1.0.so",
            "/usr/lib/x86_64-linux-gnu/libpango-1.0.so",
            "/usr/lib/x86_64-linux-gnu/libharfbuzz.so",
            "/usr/lib/x86_64-linux-gnu/libatk-1.0.so",
            "/usr/lib/x86_64-linux-gnu/libcairo-gobject.so",
            "/usr/lib/x86_64-linux-gnu/libcairo.so",
            "/usr/lib/x86_64-linux-gnu/libgdk_pixbuf-2.0.so",
            "/usr/lib/x86_64-linux-gnu/libgio-2.0.so",
            "/usr/lib/x86_64-linux-gnu/libgobject-2.0.so",
            "/usr/lib/x86_64-linux-gnu/libglib-2.0.so")


val includes = listOf(
            "-I/usr/include/glib-2.0",
            "-I/usr/include/gtk-3.0",
            "-I/usr/include/pango-1.0",
            "-I/usr/include/harfbuzz",
            "-I/usr/include/cairo",
            "-I/usr/include/gdk-pixbuf-2.0",
            "-I/usr/include/atk-1.0",
            "-I/usr/include/gio-unix-2.0",
            "-I/usr/lib/x86_64-linux-gnu/glib-2.0/include")


// gradle: the most chaotic build system ever
// https://discuss.gradle.org/t/how-to-pass-arguments-to-the-linker-when-building-a-cpp-application/30855/8
library {
//    targetMachines.add(machines.os("linux").architecture("x86-64"))
//    targetMachines.add(machines.os("windows").architecture("x86-64"))
//    targetMachines.add(machines.os("android").architecture("armv7"))


    binaries.configureEach {
        val compileTask = compileTask.get()
        compileTask.includes.from("${Jvm.current().javaHome}/include")

        val osFamily = targetPlatform.targetMachine.operatingSystemFamily
        if (osFamily.isMacOs) {
            compileTask.includes.from("${Jvm.current().javaHome}/include/darwin")
        } else if (osFamily.isLinux) {
            compileTask.includes.from("${Jvm.current().javaHome}/include/linux")
        } else if (osFamily.isWindows) {
            compileTask.includes.from("${Jvm.current().javaHome}/include/win32")
        }

        val srcDir = "src/main/c"
        val genDir = "${project.buildDir}/generated/src/main/c"

        compileTask.source.from(fileTree(srcDir) {
            include("**/*.c")
        })

        compileTask.source.from(fileTree(genDir) {
            include("**/*.c")
        })

        val jniHeaders = "${project(":library").buildDir}/generated/sources/headers/java/main"

        if (toolChain is VisualCpp) {
            compileTask.compilerArgs.add("/TC")
        } else if (toolChain is Gcc) {
            compileTask.compilerArgs.addAll(setOf(
                    "-static",
                    "-DPIC64",
                    "-x", "c",
                    "-I${jniHeaders}"))
            compileTask.compilerArgs.addAll(includes)
            if (this is CppSharedLibrary) {
                val library: CppSharedLibrary = this
                library.linkTask.get().linkerArgs.set(setOf("-pthread",
                        "-lglib-2.0",
                        "-lz",
                        "/usr/lib/x86_64-linux-gnu/libgtk-3.so",
                        "/usr/lib/x86_64-linux-gnu/libgdk-3.so",
                        "/usr/lib/x86_64-linux-gnu/libpangocairo-1.0.so",
                        "/usr/lib/x86_64-linux-gnu/libpango-1.0.so",
                        "/usr/lib/x86_64-linux-gnu/libharfbuzz.so",
                        "/usr/lib/x86_64-linux-gnu/libatk-1.0.so",
                        "/usr/lib/x86_64-linux-gnu/libcairo-gobject.so",
                        "/usr/lib/x86_64-linux-gnu/libcairo.so",
                        "/usr/lib/x86_64-linux-gnu/libgdk_pixbuf-2.0.so",
                        "/usr/lib/x86_64-linux-gnu/libgio-2.0.so",
                        "/usr/lib/x86_64-linux-gnu/libgobject-2.0.so",
                        "/usr/lib/x86_64-linux-gnu/libglib-2.0.so"))
            }
        }

    }
}