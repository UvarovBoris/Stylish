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
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Stylish"
include(":app")
include(":core:designsystem")
include(":core:database")
include(":core:datastore")
include(":core:data")
include(":core:model")
include(":core:network")
include(":core:ui")
include(":feature:onboarding")
include(":feature:main")
include(":feature:home")
include(":feature:category")
include(":feature:productdetail")
include(":feature:wishlist")