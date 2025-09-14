plugins {
    application
    id("build-jvm")
    alias(libs.plugins.shadowJar)
    id("build-docker")
}

application {
    mainClass.set("org.kotlined.app.kafka.MainKt")
}

docker {
    imageName = "contact-center-app-kafka"
}

dependencies {
    implementation(libs.kafka.client)
    implementation(libs.coroutines.core)
    implementation(libs.kotlinx.atomicfu)

    implementation(project(":contact-center-app-common"))

    // transport models
    implementation(project(":contact-center-common"))
    implementation(project(":contact-center-api-v1-jackson"))
    implementation(project(":contact-center-api-v1-mappers"))
    implementation(project(":contact-center-api-v2-multiplatform"))
    // logic
    implementation(project(":contact-center-biz"))

    testImplementation(kotlin("test-junit"))
}

tasks {
    shadowJar {
        manifest {
            attributes(mapOf("Main-Class" to application.mainClass.get()))
        }
    }

    dockerBuild {
        dependsOn("shadowJar")
    }
}

