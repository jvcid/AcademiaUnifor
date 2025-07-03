pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS) // Ou PREFER_PROJECT se quiser permitir repositórios no build.gradle
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "AcademiaUnifor"
include(":app")