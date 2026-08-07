pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "IMDBApp"
include(":app")
include(":core:network")
include(":core:common")
include(":feature:home")
include(":feature:serach")
include(":feature:watchlist")
include(":feature:profile")
include(":feature:detail")
include(":core:testing:unit")
include(":core:designsystem")
include(":core:model")
include(":core:datastore-proto")
include(":core:datastore")
include(":core:navigation")
include(":core:data")
include(":lint")
