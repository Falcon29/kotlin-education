rootProject.name = "contact-center-other"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

pluginManagement {
    includeBuild("../build-plugin")
    plugins {
        id("build-jvm") apply false
        id("build-multiplatform") apply false
    }
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

include(":contact-center-migration")
