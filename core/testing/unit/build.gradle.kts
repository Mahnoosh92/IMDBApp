plugins {
    alias(libs.plugins.android.library)
}

android {
    namespace = "com.example.unit"
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
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            all {
                it.useJUnitPlatform()
            }
        }
    }
}

dependencies {
    // Test
    api(libs.junit)
    api(libs.coroutines.test)
    api(libs.turbine)
    api(libs.mockk)
    api(libs.truth)
    api(libs.hilt.test)

    api(libs.junit.jupiter.api)
    api(libs.junit.jupiter.params)
    api(libs.junit.jupiter.engine)
    api(libs.junit.platform.launcher)
}
