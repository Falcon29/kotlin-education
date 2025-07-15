plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins {
        register("build-jvm") {
            id = "build-jvm"
            implementationClass = "BuildPluginJvm"
        }
        register("build-multiplatform") {
            id = "build-multiplatform"
            implementationClass = "BuildPluginMultiplatform"
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.plugin.kotlin)
    implementation(libs.plugin.binaryCompatibilityValidator)
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}