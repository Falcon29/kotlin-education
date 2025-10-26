plugins {
    id("build-multiplatform")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(kotlin("test-common"))
                api(kotlin("test-annotations-common"))

                api(libs.coroutines.core)
                api(libs.coroutines.test)
                implementation(projects.contactCenterCommon)
                implementation(projects.contactCenterRepoCommon)
            }
        }
        commonTest {
            dependencies {
                implementation(projects.contactCenterStubs)
            }
        }
        jvmMain {
            dependencies {
                api(kotlin("test-junit"))
            }
        }
    }
}
