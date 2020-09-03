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

val MP_PAGING_VERSION: String by rootProject.extra

val artifactName = "multiplatform-paging"
val artifactGroup = "com.kuuuurt"
val artifactVersion = MP_PAGING_VERSION

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

val COROUTINES_VERSION: String by rootProject.extra
val KTOR_VERSION: String by rootProject.extra

kotlin {
    android {
        publishAllLibraryVariants()
    }

    ios()

    sourceSets["commonMain"].dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$COROUTINES_VERSION")
        implementation("io.ktor:ktor-client-core:$KTOR_VERSION")
    }

    sourceSets["androidMain"].dependencies {
        implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.2.0")
        api("androidx.paging:paging-runtime:3.0.0-alpha01")
    }
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