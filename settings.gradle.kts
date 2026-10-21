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

rootProject.name = "HealthApp"
include(":app")
include(":core:util")
include(":features:home")
include(":features:history")
include(":features:profile")
include(":features:workout")
include(":features:auth")
include(":core:database")
include(":core:remote")
include(":core:datastore")
