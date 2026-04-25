import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    kotlin("plugin.serialization") version "1.9.20"
    id("app.cash.sqldelight") version "2.0.1"
}

kotlin {
    androidTarget()

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
            implementation("app.cash.sqldelight:android-driver:2.0.1")
            // HAPUS: multiplatform-settings-android di sini karena sudah ada di commonMain
        }

        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            // Cukup deklarasikan di sini saja
            implementation("com.russhwolf:multiplatform-settings:1.1.1")
            implementation("com.russhwolf:multiplatform-settings-coroutines:1.1.1")

            implementation("org.jetbrains.compose.material:material-icons-extended:1.6.1")
            implementation("app.cash.sqldelight:runtime:2.0.1")
            implementation("app.cash.sqldelight:coroutines-extensions:2.0.1")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")
            implementation("org.jetbrains.androidx.navigation:navigation-compose:2.8.0-alpha10")
        }

        iosMain.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
            implementation("app.cash.sqldelight:native-driver:2.0.1")
        }
    }
}

sqldelight {
    databases {
        create("NotesDatabase") {
            packageName.set("com.fanisa.upgradenote.db")
        }
    }
}

android {
    namespace = "com.fanisa.upgradenote"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.fanisa.upgradenote"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs += "-Xexpect-actual-classes"
    }
}