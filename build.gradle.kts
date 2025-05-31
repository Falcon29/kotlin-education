plugins {
    kotlin("jvm") version "2.1.20"
}

allprojects {
    group = property("group").toString()
    version = property("version").toString()

    repositories {
        mavenCentral()
    }
}