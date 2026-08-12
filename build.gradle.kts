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
