plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.multiplatform")
}

val KOTLIN_VERSION = "1.3.70"
val COROUTINES_VERSION = "1.3.3"

val frameworkName = "MultiplatformPaging"

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

kotlin {
    ios {
        binaries {
            framework(frameworkName) {
                baseName = frameworkName
            }
        }
    }
    android()

    sourceSets["commonMain"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:${COROUTINES_VERSION}")
    }

    sourceSets["iosMain"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:${COROUTINES_VERSION}")
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$COROUTINES_VERSION")
    implementation("androidx.paging:paging-runtime:2.1.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.2.0")

}
