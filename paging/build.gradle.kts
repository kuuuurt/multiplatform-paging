import com.jfrog.bintray.gradle.tasks.BintrayUploadTask
import org.gradle.api.publish.maven.internal.artifact.FileBasedMavenArtifact
import java.io.FileInputStream
import java.util.*

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.multiplatform")
    id("maven-publish")
    id("com.jfrog.bintray") version "1.8.4"
}

val artifactName = "multiplatform-paging"
val artifactGroup = "com.kuuuurt"
val artifactVersion = "0.1.3"

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

val frameworkName = "MultiplatformPaging"

group = artifactGroup
version = artifactVersion

android {
    compileSdkVersion(29)
    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(29)

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            matchingFallbacks = listOf("release")
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

val KOTLIN_VERSION = "1.3.70"
val COROUTINES_VERSION = "1.3.4"

kotlin {
    android {
        publishAllLibraryVariants()
    }

    ios {
        compilations {
            val main by getting {
                kotlinOptions.freeCompilerArgs = listOf("-Xobjc-generics")
            }
        }
    }

    sourceSets["commonMain"].dependencies {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-common")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-common:${COROUTINES_VERSION}")
        implementation("com.soywiz.korlibs.korio:korio:1.10.0")
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

bintray {
    val bintrayPropertiesFile = project.rootProject.file("bintray.properties")
    val bintrayProperties = Properties()

    bintrayProperties.load(FileInputStream(bintrayPropertiesFile))
    user = bintrayProperties.getProperty("bintray.user")
    key = bintrayProperties.getProperty("bintray.key")
    publish = true
    override = true

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

tasks.named<BintrayUploadTask>("bintrayUpload") {
    dependsOn("publishToMavenLocal")
    doFirst {
        project.publishing.publications.withType<MavenPublication>().all {
            val moduleFile = buildDir.resolve("publications/$name/module.json")
            if (moduleFile.exists()) {
                artifact(object : FileBasedMavenArtifact(moduleFile) {
                    override fun getDefaultExtension() = "module"
                })
            }
        }
    }
}

afterEvaluate {
    project.publishing.publications.withType<MavenPublication>().all {
        groupId = artifactGroup

        artifactId = if (name.contains("metadata")) {
            "$artifactName-common"
        } else if (name.contains("androidRelease")) {
            "$artifactName-android"
        } else if (name.contains("kotlinMultiplatform")) {
            artifactName
        } else {
            "$artifactName-$name"
        }
    }
    bintray {
        setPublications(*publishing.publications
            .map { it.name }
            .toTypedArray())
    }
}