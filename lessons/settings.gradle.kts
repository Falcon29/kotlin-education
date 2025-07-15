pluginManagement {
    val kotlinVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
    }

    includeBuild("../build-plugin")
}

rootProject.name = "lessons"

include("m1lesson1-begin")
include("m1lesson3-fun")
include("m1lesson4-oop")
include("m2lesson1-dsl")
include("m2lesson2-coroutines")
include("m2lesson3-flows")