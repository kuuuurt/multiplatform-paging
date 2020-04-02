import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import sun.management.ConnectorAddressLink.export

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.kotlin.native.cocoapods")
}

val KOTLIN_VERSION = "1.3.70"
val COROUTINES_VERSION = "1.3.4"
val MP_PAGING_VERSION = "0.1.2"

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
            java.srcDirs("src/androidMain/kotlin")
            res.srcDirs("src/androidMain/res")
        }
        getByName("test") {
            java.srcDirs("src/androidTest/kotlin")
            res.srcDirs("src/androidTest/res")
        }
        getByName("androidTest") {
            java.srcDirs("src/androidInstrumentedTest/kotlin")
            res.srcDirs("src/androidInstrumentedTest/res")
        }
    }
}

version = "1.0.0"

kotlin {
    cocoapods {
        summary = "Shared module for Android and iOS"
        homepage = "Link to a Kotlin/Native module homepage"
//        For 1.3.70
//        frameworkName = iosFrameworkName
    }

    ios {
        compilations {
            val main by getting {
                kotlinOptions.freeCompilerArgs = listOf("-Xobjc-generics")
            }
        }
    }
    
    targets.named<KotlinNativeTarget>("iosX64") {
        binaries.withType<Framework>().configureEach {
            export("com.kuuuurt:multiplatform-paging-iosX64:$MP_PAGING_VERSION")
        }
    }

    targets.named<KotlinNativeTarget>("iosArm64") {
        binaries.withType<Framework>().configureEach {
            export("com.kuuuurt:multiplatform-paging-iosArm64:$MP_PAGING_VERSION")
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
        implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:${COROUTINES_VERSION}")
        implementation("com.soywiz.korlibs.korio:korio:1.10.0")
        api("com.kuuuurt:multiplatform-paging:$MP_PAGING_VERSION")
    }

    sourceSets["iosMain"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:${COROUTINES_VERSION}")
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$COROUTINES_VERSION")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
}
