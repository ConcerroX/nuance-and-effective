import net.fabricmc.loom.api.LoomGradleExtensionAPI

val effectiveModId: String by project
val effectiveModVersion: String by rootProject
val effectiveModDescription: String by project
val effectiveMavenGroup: String by rootProject
val minecraftVersion: String by rootProject

plugins {
    kotlin("jvm") version "1.8.22"
    id("io.github.pacifistmc.forgix") version "1.2.9"
    id("architectury-plugin") version "3.4-SNAPSHOT"
    id("dev.architectury.loom") version "1.7-SNAPSHOT" apply false
    id("com.github.johnrengelman.shadow") version "8.1.1" apply false
}

architectury {
    minecraft = minecraftVersion
}

forgix {
    group = effectiveMavenGroup
    mergedJarName = "$effectiveModId-$effectiveModVersion.jar"
    outputDir = "build"
}

allprojects {
    group = effectiveMavenGroup
    version = effectiveModVersion
    description = effectiveModDescription
}

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "architectury-plugin")
    apply(plugin = "dev.architectury.loom")

    val loom = project.extensions.getByName<LoomGradleExtensionAPI>("loom").apply {
        silentMojangMappingsLicense()
    }

    base {
        archivesName = "$effectiveModId-${project.name}"
    }

    repositories {
        maven("https://maven.parchmentmc.org")
    }

    dependencies {
        "minecraft"("com.mojang:minecraft:$minecraftVersion")
        @Suppress("UnstableApiUsage") "mappings"(loom.layered {
            officialMojangMappings()
            parchment("org.parchmentmc.data:parchment-1.20.1:2023.09.03@zip")
        })
    }

}