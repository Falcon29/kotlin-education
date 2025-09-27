plugins {
    alias(libs.plugins.kotlin.jvm) apply false // apply false because plugins are applied for subproject, not root
    alias(libs.plugins.kotlin.multiplatform) apply false
}

group = "org.kotlined"
version = "0.0.1"

repositories {
    mavenCentral()
}

subprojects {
    repositories {
        mavenCentral()
    }
    group = rootProject.group
    version = rootProject.version
}
