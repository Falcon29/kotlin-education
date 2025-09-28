rootProject.name = "contact-center-be"

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
        id("build-docker") apply false
    }
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

//implementation(projects.m2l5Gradle.sub1.ssub1)
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":contact-center-api-v1-jackson")
include(":contact-center-api-v1-mappers")
include(":contact-center-api-v2-multiplatform")

include(":contact-center-common")
include(":contact-center-stubs")
include(":contact-center-app-ktor")
include(":contact-center-app-kafka")
include(":contact-center-biz")
include(":contact-center-app-common")
