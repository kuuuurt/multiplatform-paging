import com.android.build.gradle.internal.tasks.factory.dependsOn
import com.jfrog.bintray.gradle.tasks.BintrayUploadTask
import java.util.Date
import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.multiplatform")
    id("maven-publish")
    id("com.jfrog.bintray")
}
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

val KOTLIN_VERSION = "1.3.61"
val KOTLINX_IO_VERSION = "0.1.16"
val COROUTINES_VERSION = "1.3.3"

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

val frameworkName = "MultiplatformPaging"

val artifactName = "multiplatform-paging"
val artifactGroup = "com.kuuuurt"
val artifactVersion = "0.1.0"

val pomUrl = "https://github.com/kuuuurt/multiplatform-paging"
val pomScmUrl = "https://github.com/kuuuurt/multiplatform-paging.git"
val pomIssueUrl = "https://github.com/kuuuurt/multiplatform-paging/issues"
val pomDesc = "A Kotlin Multiplatform library for pagination on Android and iOS"

val githubRepo = "kuuuurt/multiplatform-paging"
val githubReadme = "README.md"

val pomLicenseName = "Apache-2.0"
val pomLicenseUrl = "https://www.apache.org/licenses/LICENSE-2.0"
val pomLicenseDist = "repo"

val pomDeveloperId = "kuuuurt"
val pomDeveloperName = "Kurt Renzo Acosta"

group = artifactGroup
version = artifactVersion


publishing {
    publications.withType<MavenPublication>().forEach {
        it.pom.withXml {
            asNode().apply {
                appendNode("description", pomDesc)
                appendNode("name", rootProject.name)
                appendNode("url", pomUrl)
                appendNode("licenses").appendNode("license").apply {
                    appendNode("name", pomLicenseName)
                    appendNode("url", pomLicenseUrl)
                    appendNode("distribution", pomLicenseDist)
                }
                appendNode("developers").appendNode("developer").apply {
                    appendNode("id", pomDeveloperId)
                    appendNode("name", pomDeveloperName)
                }
                appendNode("scm").apply {
                    appendNode("url", pomScmUrl)
                }
            }
        }
    }
}

afterEvaluate {
    project.publishing.publications.withType<MavenPublication>().all {
        groupId = artifactGroup
        artifactId = if (name.contains("metadata")) {
            artifactName
        } else {
            "$artifactName-$name"
        }
    }
}

bintray {
    val bintrayPropertiesFile = project.rootProject.file("bintray.properties")
    val bintrayProperties = Properties()

    bintrayProperties.load(FileInputStream(bintrayPropertiesFile))
    user = bintrayProperties.getProperty("bintray.user")
    key = bintrayProperties.getProperty("bintray.key")
    publish = true

    val pubs = publishing.publications
        .map { it.name }
        .filter { it != "kotlinMultiplatform" }
        .toTypedArray()
    setPublications(*pubs)

    pkg.apply {
        repo = "libraries"
        name = artifactName
        websiteUrl = pomUrl
        githubRepo = "kuuuurt/multiplatform-paging"
        vcsUrl = pomScmUrl
        description = ""
        setLabels("kotlin", "multiplatform", "android", "ios")
        setLicenses("Apache-2.0")
        desc = description
        issueTrackerUrl = pomIssueUrl

        version.apply {
            name = artifactVersion
            vcsTag = artifactVersion
            released = Date().toString()
        }
    }
}

tasks.named<BintrayUploadTask>("bintrayUpload").dependsOn("publishToMavenLocal")