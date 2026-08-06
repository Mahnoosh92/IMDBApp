plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.devtools.ksp)
}

android {
    namespace = "com.example.imdbapp"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.imdbapp"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/LICENSE.md"
            excludes += "/META-INF/LICENSE-notice.md"
        }
    }
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.model)
    implementation(projects.core.datastore)
    implementation(projects.core.navigation)
    implementation(projects.feature.home)
    implementation(projects.feature.profile)
    implementation(projects.feature.watchlist)
    implementation(projects.feature.serach)
    implementation(projects.feature.detail)

    // di
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // performance
    implementation(libs.androidx.metrics)

    // splash
    implementation(libs.androidx.core.splashscreen)

    // adaptive window
    implementation(libs.androidx.compose.material3.adaptive)

    // navigation suite
    implementation(libs.androidx.compose.material3.navigationSuite)

}
