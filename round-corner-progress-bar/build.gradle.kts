plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.maven.publish)
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

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.annotation)
    implementation(libs.customview.poolingcontainer)
}

mavenPublishing {
    coordinates("com.akexorcist", "round-corner-progress-bar", "2.2.0")
    pom {
        name = "Round Corner Progress Bar"
        description = "A progress bar with round corner for Android."
        url = "https://github.com/akexorcist/Android-RoundCornerProgressBar"
        licenses {
            license {
                name = "The Apache Software License, Version 2.0"
                url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "repo"
            }
        }
        developers {
            developer {
                id = "akexorcist"
                name = "Akexorcist"
                email = "akexorcist@gmail.com"
            }
        }
        scm {
            connection = "scm:git:github.com/akexorcist/Android-RoundCornerProgressBar.git"
            developerConnection = "scm:git:ssh://github.com:akexorcist/Android-RoundCornerProgressBar.git"
            url = "https://github.com/akexorcist/Android-RoundCornerProgressBar/tree/master"
        }
    }
}
