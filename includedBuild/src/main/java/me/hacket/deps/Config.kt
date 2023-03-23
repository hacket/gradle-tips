package me.hacket.deps

object VersionsComposing {
    const val kotlin = "1.4.20"
    const val ktx = "1.0.0"
    const val coroutines = "1.3.2"
    const val gradlePlugin = "4.1.0"
    const val lifecycle = "2.2.0"

    const val compileSdkVersion = 32
    const val minSdkVersion = 19
    const val targetSdkVersion = 32
    const val versionCode = 1
    const val versionName = "1.0.0"
}

object DepsComposing {

    object Kotlin {
        const val stdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${VersionsComposing.kotlin}"
        const val coroutines =
            "org.jetbrains.kotlinx:kotlinx-coroutines-core:${VersionsComposing.coroutines}"
        const val coroutinesAndroid =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:${VersionsComposing.coroutines}"
        const val ktxCore = "androidx.core:core-ktx:${VersionsComposing.ktx}"
    }

    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:1.2.0"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:1.1.3"
        const val recyclerview = "androidx.recyclerview:recyclerview:1.1.0"
        const val extension = "androidx.lifecycle:lifecycle-extensions:${VersionsComposing.lifecycle}"
        const val livedata = "androidx.lifecycle:lifecycle-livedata:${VersionsComposing.lifecycle}"
        const val lifecycleRuntime =
            "androidx.lifecycle:lifecycle-runtime-ktx:${VersionsComposing.lifecycle}"
        const val multidex = "androidx.multidex:multidex:2.0.1"
    }
}

object ClassPathsComposing {
    const val gradlePlugin = "com.android.tools.build:gradle:${VersionsComposing.gradlePlugin}"
    const val gradleApi = "com.android.tools.build:gradle-api:${VersionsComposing.gradlePlugin}"
    const val kotlinPlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${VersionsComposing.kotlin}"
    const val vanniktechMavenPublishPlugin = "com.vanniktech:gradle-maven-publish-plugin:0.14.0"
}
