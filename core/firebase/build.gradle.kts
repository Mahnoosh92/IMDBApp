plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.devtools.ksp)
}

android {
    namespace = "com.example.firebase"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
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
}

dependencies {
    implementation(projects.core.designsystem)
    implementation(projects.core.navigation)
    // di
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // firebase
    api(platform(libs.firebase.bom))
    api(libs.firebase.inappmessaging)
    implementation(libs.play.services.basement)

    // Firebase Crashlytics & Analytics (Analytics enables breadcrumb logging)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)

    // Firebase Performance Monitoring
    api(libs.firebase.perf)
}
