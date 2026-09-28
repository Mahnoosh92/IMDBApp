// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.dagger.hilt.android) apply false
    alias(libs.plugins.devtools.ksp) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.junit5) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.spotless) apply false
    id("com.google.gms.google-services") version "4.5.0" apply false
    id("com.google.android.gms.oss-licenses-plugin") version "0.13.0" apply false
}

subprojects {
    apply(plugin = "com.diffplug.spotless")

    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        kotlin {
            target("**/*.kt")
            targetExclude("**/build/**/*.kt")

            ktlint("1.0.1")
                .setEditorConfigPath(rootProject.file(".editorconfig"))
                .editorConfigOverride(
                    mapOf(
                        "max_line_length" to "300",
                        "ktlint_standard_max-line-length" to "disabled",
                        "ktlint_standard_discouraged-comment-location" to "disabled"
                    )
                )

            trimTrailingWhitespace()
            leadingTabsToSpaces()
            endWithNewline()
        }

        kotlinGradle {
            target("*.gradle.kts")
            ktlint("1.0.1").setEditorConfigPath(rootProject.file(".editorconfig"))
        }

        format("xml") {
            target("**/*.xml")
            targetExclude("**/build/**/*.xml")
        }
    }

    configurations.all {
        exclude(group = "com.google.protobuf", module = "protobuf-java")
        exclude(group = "com.google.api.grpc", module = "proto-google-common-protos")
        exclude(group = "com.sun.activation", module = "javax.activation")

        resolutionStrategy.eachDependency {
            if (requested.group == "androidx.compose.foundation" && requested.name == "foundation-layout") {
                useVersion("1.7.6") // Or any version compatible with composeBom 2024.12.01
            }
        }
    }
}

tasks.register<Copy>("installGitHooks") {
    from(file("scripts/pre-commit"))
    into(file(".git/hooks"))
    filePermissions {
        unix("rwxr-xr-x")
    }
}

afterEvaluate {
    tasks.named("prepareKotlinBuildScriptModel") {
        dependsOn("installGitHooks")
    }
}
