plugins {
    alias(libs.plugins.kotlin.jvm) apply false // apply false because plugins are applied for subproject, not root
    alias(libs.plugins.kotlin.multiplatform) apply false
    id("build-docker") apply false
}

group = "org.kotlined.cc"
version = "0.0.1"

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version
}

ext {
    val specDir = layout.projectDirectory.dir("../specs")
    set("spec-v1", specDir.file("spec-cc-v1.yaml").toString())
    set("spec-v2", specDir.file("spec-cc-v2.yaml").toString())
    set("spec-log", specDir.file("spec-cc-log.yaml").toString())
}

tasks {
    register("build" ) {
        group = "build"
    }
    register("check" ) {
        group = "verification"
        subprojects.forEach { proj ->
            println("PROJ $proj")
            proj.getTasksByName("check", false).also {
                this@register.dependsOn(it)
            }
        }
    }
    register("buildImages") {
        dependsOn(project("contact-center-app-ktor").tasks.getByName("publishImageToLocalRegistry"))
        dependsOn(project("contact-center-app-ktor").tasks.getByName("dockerBuildX64Image"))
    }
}