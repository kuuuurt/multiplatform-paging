import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.kotlin.native.cocoapods")
}

val COROUTINES_VERSION: String by rootProject.extra
val MP_PAGING_VERSION: String by rootProject.extra

val iosFrameworkName = "MultiplatformPaging"

android {
    compileSdkVersion(29)
    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(29)

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            consumerProguardFiles("consumer-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    sourceSets {
        getByName("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
        }
    }
}

version = "1.0.0"

kotlin {
    cocoapods {
        summary = "Shared module for Android and iOS"
        homepage = "Link to a Kotlin/Native module homepage"
        frameworkName = iosFrameworkName
    }

    ios()
    
    targets.named<KotlinNativeTarget>("iosX64") {
        binaries.withType<Framework>().configureEach {
            export("io.github.kuuuurt:multiplatform-paging-iosX64:$MP_PAGING_VERSION")
        }
    }

    targets.named<KotlinNativeTarget>("iosArm64") {
        binaries.withType<Framework>().configureEach {
            export("io.github.kuuuurt:multiplatform-paging-iosArm64:$MP_PAGING_VERSION")
        }
    }

    //    If not using target shortcut
//
//    val isDevice = System.getenv("SDK_NAME")?.startsWith("iphoneos") == true
//    val pagingIos: String
//    val iosTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget
//    if (isDevice) {
//        iosTarget = ::iosArm64
//        pagingIos = "com.kuuuurt:multiplatform-paging-iosArm64:$MP_PAGING_VERSION"
//    } else {
//        iosTarget = ::iosX64
//        pagingIos = "com.kuuuurt:multiplatform-paging-iosX64:$MP_PAGING_VERSION"
//    }
//
//    iosTarget("ios") {
//        compilations {
//            val main by getting {
//                kotlinOptions.freeCompilerArgs = listOf("-Xobjc-generics")
//            }
//        }
//        binaries.withType<Framework>().configureEach {
//            export(pagingIos)
//        }
//    }

    android()

    sourceSets["commonMain"].dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$COROUTINES_VERSION")
        api("io.github.kuuuurt:multiplatform-paging:$MP_PAGING_VERSION")
    }

    sourceSets["androidMain"].dependencies {
        implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
    }
}
