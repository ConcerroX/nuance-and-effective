pluginManagement {
    plugins {
        id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
    }
    repositories {
        gradlePluginPortal()
        maven("https://maven.architectury.dev/")
        maven("https://maven.fabricmc.net/")
        maven("https://maven.neoforged.net/releases/")
    }
}

include(":effective", ":effective:common", ":effective:fabric", ":effective:neoforge")

rootProject.name = "nuance-and-effective"