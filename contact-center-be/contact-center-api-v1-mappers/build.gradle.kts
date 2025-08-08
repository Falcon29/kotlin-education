plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib"))
    implementation(projects.contactCenterApiV1Jackson)
    implementation(projects.contactCenterCommon)

    testImplementation(kotlin("test-junit"))
//    testImplementation(projects.contactCenterStubs)
}