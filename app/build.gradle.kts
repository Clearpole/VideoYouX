plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

fun buildInfo(type: String): Any? {
    when (type) {
        "name" -> {
            return "VideoYouX"
        }

        "isCanary" -> {
            return "true"
        }

        "version" -> {
            return "0.9.9"
        }

        "subVersion" -> {
            return "Canary06"
        }

        else -> {
            return null
        }
    }
}

android {
    namespace = "com.clearpole.videoyoux"
    compileSdk = 33
    compileSdkPreview = "UpsideDownCake"
    defaultConfig {
        applicationId = "com.clearpole.videoyoux"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = if (buildInfo("isCanary") == "true") {
            buildInfo("version").toString() + " - " + buildInfo("subVersion").toString()
        } else {
            buildInfo("version").toString()
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
             abiFilters += listOf("arm64-v8a")
        }
    }

    sourceSets {
        getByName("main") {
            jniLibs.srcDirs("libs")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
    android.applicationVariants.all {
        outputs.all {
            if (this is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
                this.outputFileName = if (buildInfo("isCanary") == "false") {
                    buildInfo("name").toString() + "-release.apk"
                } else {
                    buildInfo("name").toString() + "-" + buildInfo("subVersion").toString() + ".apk"
                }
            }
        }
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.viewBing.ktx)
    implementation(libs.viewBing.base)
    implementation(libs.androidx.palette)
    implementation(libs.monet.compat)
    implementation(libs.immersionbar)
    implementation(libs.utilcodex)
}