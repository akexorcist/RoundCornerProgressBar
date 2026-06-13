plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.maven.publish)
    id("signing")
}

android {
    namespace = "com.akexorcist.roundcornerprogressbar.compose"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdkCompose.get().toInt()
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.foundation)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)

    testImplementation(libs.junit)
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()
    coordinates("com.akexorcist", "round-corner-progress-bar-compose", libs.versions.libraryVersion.get())
    pom {
        name.set("Round Corner Progress Bar Compose")
        description.set("A progress bar with round corner for Jetpack Compose.")
        url.set("https://github.com/akexorcist/Android-RoundCornerProgressBar")
        licenses {
            license {
                name.set("The Apache Software License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("repo")
            }
        }
        developers {
            developer {
                id.set("akexorcist")
                name.set("Akexorcist")
                email.set("akexorcist@gmail.com")
            }
        }
        scm {
            connection.set("scm:git:github.com/akexorcist/Android-RoundCornerProgressBar.git")
            developerConnection.set("scm:git:ssh://github.com:akexorcist/Android-RoundCornerProgressBar.git")
            url.set("https://github.com/akexorcist/Android-RoundCornerProgressBar")
        }
    }
}
