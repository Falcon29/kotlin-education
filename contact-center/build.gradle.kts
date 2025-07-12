plugins {
    alias(libs.plugins.kotlin.jvm) apply false // apply false because plugins are applied for subproject, not root
    alias(libs.plugins.kotlin.multiplatform) apply false
}

group = "org.kotlined"
version = "0.1"
