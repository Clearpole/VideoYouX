import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
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
            return "Canary05"
        }

        else -> {
            return null
        }
    }
}

android {
    namespace = "com.videoyou.x"
    compileSdk = 34
    //compileSdkPreview = ""
    defaultConfig {
        applicationId = namespace
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = if (buildInfo("isCanary") == "true") {
            buildInfo("version").toString() + " - " + buildInfo("subVersion").toString()
        } else {
            buildInfo("version").toString()
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        resourceConfigurations += arrayListOf("en-rUS","zh-rCN","de-rDE","fr-rFR")

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
            create("release") {
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
                signingConfig = signingConfigs.getByName("release")
            }
        }
        debug {
            if (keystorePath != null) {
                signingConfig = signingConfigs.getByName("release")
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
    }

    buildFeatures {
        viewBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildToolsVersion = "34.0.0"
    android.applicationVariants.all {
        outputs.all {
            if (this is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
                val code = getGitHeadRefsSuffix(rootProject)
                this.outputFileName = if (buildInfo("isCanary") == "false") {
                    buildInfo("name").toString() + buildInfo("version") + "-$code-${buildType.name}.apk"
                } else {
                    buildInfo("name").toString() + "-" + buildInfo("version") + "-" + buildInfo("subVersion").toString() + "-$code-${buildType.name}.apk"
                }
            }
        }
    }
}

//来自 https://github.com/qqlittleice/MiuiHome_R/blob/main/app/build.gradle.kts#L81
fun getGitHeadRefsSuffix(project: Project): String {
    // .git/HEAD 描述当前目录所指向的分支信息，内容示例："ref: refs/heads/master\n"
    val headFile = File(project.rootProject.projectDir, ".git" + File.separator + "HEAD")
    if (headFile.exists()) {
        val string: String = headFile.readText(Charsets.UTF_8)
        val string1 = string.replace(Regex("""ref:|\s"""), "")
        val result = if (string1.isNotBlank() && string1.contains('/')) {
            val refFilePath = ".git" + File.separator + string1
            // 根据 HEAD 读取当前指向的 hash 值，路径示例为：".git/refs/heads/master"
            val refFile = File(project.rootProject.projectDir, refFilePath)
            // 索引文件内容为 hash 值 +"\n"，
            // 示例："90312cd9157587d11779ed7be776e3220050b308\n"
            refFile.readText(Charsets.UTF_8).replace(Regex("""\s"""), "").subSequence(0, 8)
        } else {
            string.substring(0, 8)
        }
        println("commit_id: $result")
        return "$result"
    } else {
        println("WARN: .git/HEAD does NOT exist")
        return ""
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
}