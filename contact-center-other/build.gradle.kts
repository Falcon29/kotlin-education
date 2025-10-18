plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    id("build-docker") apply false
}

group = "org.kotlined.cc.migrations"
version = "0.1.0"

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version
}

tasks {
    arrayOf("buildImages", "clean").forEach {tsk ->
        register(tsk) {
            group = "build"
            dependsOn(subprojects.map {  it.getTasksByName(tsk,false)})
        }
    }
}
