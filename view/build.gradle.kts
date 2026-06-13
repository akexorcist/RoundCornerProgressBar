plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.maven.publish)
    id("signing")
}

android {
    namespace = "com.akexorcist.roundcornerprogressbar"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.annotation)
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()
    coordinates("com.akexorcist", "roundcornerprogressbar-view", libs.versions.libraryVersion.get())
    pom {
        name.set("Round Corner Progress Bar")
        description.set("A progress bar with round corner for Android.")
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
