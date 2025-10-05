rootProject.name = "contact-center-tests"

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
        id("build-multiplarform") apply false
    }
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

//include(":contact-center-api-v1-jackson")
//include(":contact-center-api-v2-kmp")
include(":contact-center-e2e-be")
