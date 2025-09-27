import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
    id("build-multiplatform")
//    alias(libs.plugins.openapi.generator)
    alias(libs.plugins.crowdproj.generator)
    alias(libs.plugins.kotlinx.serialization)
}

crowdprojGenerate {
    packageName.set("${project.group}.api.v2")
    inputSpec.set(rootProject.ext["spec-v2"] as String)
}

//openApiGenerate {
//    val openapiGroup = "${rootProject.group}.api.v2"
//    generatorName.set("kotlin")
//    packageName.set(openapiGroup)
//    apiPackage.set("$openapiGroup.api")
//    modelPackage.set("$openapiGroup.models")
//    invokerPackage.set("$openapiGroup.invoker")
//    inputSpec.set(rootProject.ext["spec-v2"] as String)
//
//    globalProperties.apply {
//        put("models", "")
//        put("modelDocs", "false")
//    }
//
//    configOptions.set(
//        mapOf(
//            "dateLibrary" to "string",
//            "enumPropertyNaming" to "UPPERCASE",
//            "serializationLibrary" to "jackson",
//            "collectionType" to "list"
//        )
//    )
//}

kotlin {
    sourceSets {
        val serializationVersion: String by project
        val commonMain by getting {
            kotlin.srcDirs(layout.buildDirectory.dir("generate-resources/src/commonMain/kotlin"))
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(projects.contactCenterCommon)

                implementation(libs.kotlinx.serialization.core)
                implementation(libs.kotlinx.serialization.json)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(projects.contactCenterStubs)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}

tasks {
    val openApiGenerateTask: GenerateTask = getByName("openApiGenerate", GenerateTask::class) {
        outputDir.set(layout.buildDirectory.file("generate-resources").get().toString())
        finalizedBy("compileCommonMainKotlinMetadata")
    }
    filter { it.name.startsWith("compile") }.forEach {
        it.dependsOn(openApiGenerateTask)
    }
}
