pluginManagement {
    plugins {
        val kotlinVersion: String by settings
        kotlin("jvm") version kotlinVersion
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

rootProject.name = "kotlined"

//includeBuild("lessons")
includeBuild("contact-center-be")
includeBuild("contact-center-libs")
includeBuild("contact-center-e2e-tests")
includeBuild("contact-center-other")
//include("contact-center-be:contact-center-repo-common")
//include("contact-center-be:contact-center-repo-cassandra")
//include("contact-center-be:contact-center-repo-inmemory")
//include("contact-center-be:contact-center-repo-stubs")
//include("contact-center-be:contact-center-repo-tests")