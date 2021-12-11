plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.kotlin.native.cocoapods")
}

val COROUTINES_VERSION: String by rootProject.extra
val MP_PAGING_VERSION: String by rootProject.extra

val iosFrameworkName = "MultiplatformPaging"

android {
    compileSdk = 31
    defaultConfig {
        minSdk = 21
        targetSdk = 31

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
    android()
    ios()
    iosSimulatorArm64()

    cocoapods {
        summary = "Shared module for Android and iOS"
        homepage = "Link to a Kotlin/Native module homepage"


        framework {
            baseName = iosFrameworkName
            export("io.github.kuuuurt:multiplatform-paging:$MP_PAGING_VERSION")
        }
    }

    val commonMain by sourceSets.getting {
        dependencies {
            api("io.github.kuuuurt:multiplatform-paging:$MP_PAGING_VERSION")
            implementation("io.ktor:ktor-client-core:1.6.6")
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$COROUTINES_VERSION") {
                version {
                    strictly("1.5.2-native-mt")
                }
            }
        }
    }

    val androidMain by sourceSets.getting {
        dependencies {
            implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.0")
        }
    }

    val iosMain by sourceSets.getting
    val iosTest by sourceSets.getting
    val iosSimulatorArm64Main by sourceSets.getting
    val iosSimulatorArm64Test by sourceSets.getting

    // Set up dependencies between the source sets
    iosSimulatorArm64Main.dependsOn(iosMain)
    iosSimulatorArm64Test.dependsOn(iosTest)
}

