plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.multiplatform")
    id("maven-publish")
    id("com.jfrog.bintray")
}

val KOTLIN_VERSION = "1.3.61"
val KOTLINX_IO_VERSION = "0.1.16"
val COROUTINES_VERSION = "1.3.3"

val frameworkName = "MultiplatformPaging"

val GROUP_ID = "com.kuuurt"
val ARTIFACT_ID = "multiplatform-paging"

val BINTRAY_REPOSITORY = "multiplatform-paging"
val BINTRAY_ORGINIZATION= "kuuuurt"

val ISSUE_URL = "https://github.com/kuuuurt/multiplatform-paging/issues"
val SITE_URL = "https://github.com/kuuuurt/multiplatform-paging"
val VCS_URL= "https://github.com/kuuuurt/multiplatform-paging.git"
val LIBRARY_VERSION_NAME= "0.1.0"

bintray {
    user = "kuuuurt"
    key = findProperty("bintrayApiKey")
    publish = true
    setPublications(frameworkName)
    pkg(delegateClosureOf<com.jfrog.bintray.gradle.BintrayExtension.PackageConfig> {
        repo = "MultiplatformPaging"
        name = "kuuuurt"
        userOrg = "kuuuurt"
        websiteUrl = SITE_URL
        githubRepo = "kuuuurt/multiplatform-paging"
        vcsUrl = VCS_URL
        description = ""
        setLabels("kotlin")
        setLicenses("MIT")
        desc = description
    })
}

publishing {
    repositories {
        maven {
            url = uri("test")
        }
    }
}

android {
    compileSdkVersion(29)
    valaultConfig {
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
        compilations {
            val main by getting {
                kotlinOptions.freeCompilerArgs = listOf("-Xobjc-generics")
            }
        }
    }
    android {
        publishLibraryVariants("release")
    }

    sourceSets["commonMain"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:${COROUTINES_VERSION}")
        implementation("org.jetbrains.kotlinx:kotlinx-io:$KOTLINX_IO_VERSION")
    }

    sourceSets["iosMain"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-native:${COROUTINES_VERSION}")
        implementation("org.jetbrains.kotlinx:kotlinx-io-native:$KOTLINX_IO_VERSION")
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$COROUTINES_VERSION")
    implementation("androidx.paging:paging-runtime:2.1.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-io-jvm:$KOTLINX_IO_VERSION")

}
