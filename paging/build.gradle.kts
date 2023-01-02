plugins {
    id("org.jetbrains.kotlin.multiplatform")
}

apply(from = "$rootDir/gradle/versions.gradle")
apply(from = "$rootDir/gradle/publish-module.gradle")


val MP_PAGING_VERSION = ext["MP_PAGING_VERSION"]
val COROUTINES_VERSION = ext["COROUTINES_VERSION"]

val frameworkName = "MultiplatformPaging"

kotlin {
    ios()
    iosSimulatorArm64()

    jvm { compilations.all { kotlinOptions.jvmTarget = "11" } }

    val commonMain by sourceSets.getting {
        dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$COROUTINES_VERSION")
        }
    }

    val jvmMain by sourceSets.getting {
        dependencies {
            api("androidx.paging:paging-common-ktx:3.1.1")
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
