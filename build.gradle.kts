// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.3.0" apply false
    id("org.jetbrains.kotlin.android") version "2.1.0" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
    id("com.android.library") version "8.3.0" apply false
}

// Remova a seção subprojects/repositories do build.gradle.kts
// pois os repositórios já estão definidos no settings.gradle.kts

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}