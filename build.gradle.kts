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

tasks {
    register("clean") {
        group = "build"
        gradle.includedBuilds.forEach {
            dependsOn(it.task(":clean"))
        }
    }
    val buildMigrations: Task by creating {
        dependsOn(gradle.includedBuild("contact-center-other").task(":buildImages"))
    }

    val buildImages: Task by creating {
        dependsOn(buildMigrations)
        dependsOn(gradle.includedBuild("contact-center-be").task(":buildImages"))
    }

    val e2eTests: Task by creating {
        dependsOn(buildImages)
        dependsOn(gradle.includedBuild("contact-center-e2e-tests").task(":e2eTests"))
        mustRunAfter(buildImages)
    }

    register("check") {
        group = "verification"
        dependsOn(gradle.includedBuild("contact-center-be").task(":check"))
    }
}