plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("org.kotlined.cc:contact-center-api-v1-jackson")
    implementation("org.kotlined.cc:contact-center-api-v1-mappers")
    implementation("org.kotlined.cc:contact-center-api-v2-multiplatform")
    implementation("org.kotlined.cc:contact-center-stubs")

    testImplementation(kotlin("test-junit5"))

    testImplementation(libs.logback)
    testImplementation(libs.kermit)

    testImplementation(libs.bundles.kotest)

    testImplementation(libs.testcontainers.core)
    testImplementation(libs.coroutines.core)

    testImplementation(libs.ktor.client.core)
    testImplementation(libs.ktor.client.okhttp)
}

var severity: String = "MINOR"

tasks {
    withType<Test>().configureEach {
        useJUnitPlatform()
//        dependsOn(gradle.includedBuild(":contact-center-app-ktor").task("publishImageToLocalRegistry"))
//        dependsOn(gradle.includedBuild(":contact-center-app-kafka").task("dockerBuildImage"))
    }
}
