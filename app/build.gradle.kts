import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
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
    //compileSdkPreview = ""
    defaultConfig {
        applicationId = namespace
        minSdk = 31
        targetSdk = 34
        versionCode = 1
        versionName = if (buildInfo("isCanary") == true) {
            buildInfo("version").toString() + "." + "r" + buildInfo("numberOfCommits").toString() + "." + "cancry" + "." + buildInfo("shortCommitId").toString()
        } else {
            buildInfo("version").toString() + "." + "r" + buildInfo("numberOfCommits").toString() + "." + "release"
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        resourceConfigurations += arrayListOf("en","zh-rCN","de","fr","ja","zh-rTW")

        ndk {
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

    sourceSets {
        getByName("main") {
            jniLibs.srcDirs("libs")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += listOf(
            "-P", "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true"
        )
    }

    buildFeatures {
        dataBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildToolsVersion = "34.0.0"

    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }

    android.applicationVariants.all {
        outputs.all {
            if (this is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
                this.outputFileName = if (buildInfo("isCanary") == false) {
                    buildInfo("name").toString() + "-" + "v" + buildInfo("version") + "-" + "r" + buildInfo("numberOfCommits").toString() + "-" + "${buildType.name}.apk"
                } else {
                    buildInfo("name").toString() + "-" + "v" + buildInfo("version") + "-" + "r" + buildInfo("numberOfCommits").toString() + "-" + buildInfo("shortCommitId").toString() + "-" + "${buildType.name}.apk"
                }
            }
        }
    }
}

dependencies {
    implementation(libs.activity.compose)
    implementation(libs.androidx.animation)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.appcompat.resources)
    implementation(libs.google.material)
    implementation(libs.androidx.media3.session)
    implementation(libs.androidx.ui.viewbinding)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.exoplayer.dash)
    implementation(libs.androidx.media3.ui)
    implementation(libs.immersionbar)
    implementation(libs.utilcodex)
    implementation(libs.serialize)
    implementation(libs.brv)
    implementation(libs.glide)
    implementation(libs.lottie)
    implementation(libs.xx.permissions)
    implementation(libs.collapsingtoolbarlayout.subtitle)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.legacy.support.v4)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
}