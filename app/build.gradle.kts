@file:Suppress("UnstableApiUsage")

import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose.compiler)
    id("kotlin-kapt")
}

fun buildInfo(type: String): Any? {
    when (type) {
        "name" -> {
            return "VideoYouX"
        }

        "shortCommitId" -> {
            return exec("git rev-parse --short HEAD")
        }

        "numberOfCommits" -> {
            return exec("git rev-list --count HEAD")
        }

        "isCanary" -> {
            val isCanaryBuild = Properties().getProperty("GITHUB_ACTIONS") ?: System.getenv("GITHUB_ACTIONS")
            return isCanaryBuild == "true"
        }

        "version" -> {
            return "0.1.0"
        }

        else -> {
            return null
        }
    }
}

fun Project.exec(command: String): String = providers.exec {
    commandLine(command.split(" "))
}.standardOutput.asText.get().trim()

android {
    namespace = "com.videoyou.x"
    compileSdk = 34
    buildToolsVersion = "34.0.0"

    defaultConfig {
        applicationId = namespace
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = if (buildInfo("isCanary") == true) {
            "${buildInfo("version")}.r${buildInfo("numberOfCommits")}.canary.${buildInfo("shortCommitId")}"
        } else {
            "${buildInfo("version")}.r${buildInfo("numberOfCommits")}.release"
        }

        resourceConfigurations += arrayListOf("en", "zh-rCN", "de", "fr", "ja", "zh-rTW")

        ndk {
            //noinspection ChromeOsAbiSupport
            abiFilters += "arm64-v8a"
        }
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    val properties = Properties()
    runCatching { properties.load(project.rootProject.file("local.properties").inputStream()) }
    val keystorePath = properties.getProperty("KEYSTORE_PATH") ?: System.getenv("KEYSTORE_PATH")
    val keystorePwd = properties.getProperty("KEYSTORE_PASS") ?: System.getenv("KEYSTORE_PASS")
    val alias = properties.getProperty("KEY_ALIAS") ?: System.getenv("KEY_ALIAS")
    val pwd = properties.getProperty("KEY_PASSWORD") ?: System.getenv("KEY_PASSWORD")
    if (keystorePath != null) {
        signingConfigs {
            create("Vyx") {
                storeFile = file(keystorePath)
                storePassword = keystorePwd
                keyAlias = alias
                keyPassword = pwd
                enableV3Signing = true
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
            if (keystorePath != null) {
                signingConfig = signingConfigs.getByName("Vyx")
            }
        }
        debug {
            if (keystorePath != null) {
                signingConfig = signingConfigs.getByName("Vyx")
            }
        }
    }

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(17)
        }
    }

    kotlin {
        jvmToolchain(17)
        compilerOptions {
            freeCompilerArgs = listOf(
                "-Xno-param-assertions",
                "-Xno-call-assertions",
                "-Xno-receiver-assertions"
            )
        }
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/**"
            excludes += "/kotlin/**"
            excludes += "/*.bin"
            excludes += "/*.json"
            excludes += "/*.txt"
        }
    }

    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }

    android.applicationVariants.all {
        outputs.all {
            if (this is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
                this.outputFileName = if (buildInfo("isCanary") == false) {
                    buildInfo("name").toString() + "-" + "v" + buildInfo("version") + "-" + "r" + buildInfo(
                        "numberOfCommits"
                    ).toString() + "-" + "${buildType.name}.apk"
                } else {
                    buildInfo("name").toString() + "-" + "v" + buildInfo("version") + "-" + "r" + buildInfo(
                        "numberOfCommits"
                    ).toString() + "-" + buildInfo("shortCommitId").toString() + "-" + "${buildType.name}.apk"
                }
            }
        }
    }
}

dependencies {
    implementation(libs.androidx.media3.session)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.exoplayer.dash)
    implementation(libs.androidx.media3.ui)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.viewbinding)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.appcompat.resources)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment)

    implementation(libs.google.material)
    implementation(libs.collapsingtoolbarlayout.subtitle)
    implementation(libs.utilcodex)
    implementation(libs.serialize)
    implementation(libs.brv)
    implementation(libs.glide)
    implementation(libs.lottie)
    implementation(libs.xx.permissions)
    implementation(libs.immersionbar)
}
