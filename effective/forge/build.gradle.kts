import org.gradle.internal.extensions.core.extra

val forgeVersion: String by rootProject
val mixinExtrasVersion: String by rootProject
val kotlinForForgeVersion: String by rootProject
val configuredForgeVersion: String by rootProject

plugins {
    id("com.github.johnrengelman.shadow")
}

architectury {
    platformSetupLoomIde()
    forge()
}

loom {
    accessWidenerPath = project(":effective:common").loom.accessWidenerPath
    forge {
        mixinConfig("effective-common.mixins.json")
    }
}

val common: Configuration by configurations.creating
val shadowCommon: Configuration by configurations.creating
val developmentForge: Configuration by configurations.getting

@Suppress("UnstableApiUsage")
configurations {
    compileOnly.configure { extendsFrom(common) }
    runtimeOnly.configure { extendsFrom(common) }
    developmentForge.extendsFrom(common)
}

repositories {
    maven("https://thedarkcolour.github.io/KotlinForForge/")
}

dependencies {
    forge("net.minecraftforge:forge:$forgeVersion")
    implementation("thedarkcolour:kotlinforforge:$kotlinForForgeVersion")
    compileOnly(annotationProcessor("io.github.llamalad7:mixinextras-common:$mixinExtrasVersion")!!)
    implementation(include("io.github.llamalad7:mixinextras-forge:$mixinExtrasVersion")!!)
    modLocalRuntime("curse.maven:configured-457570:$configuredForgeVersion")

    common(project(":effective:common", "namedElements")) { isTransitive = false }
    shadowCommon(project(":effective:common", "transformProductionForge")) { isTransitive = false }
}

tasks.processResources {
    inputs.property("version", project.version)
    filesMatching("META-INF/mods.toml") {
        expand(mapOf(
            "name" to rootProject.extra["effectiveModName"],
            "version" to project.version,
            "description" to project.description
        ))
    }
}

tasks.shadowJar {
    configurations = listOf(shadowCommon)
    archiveClassifier.set("dev-shadow")
}

tasks.remapJar {
    inputFile.set(tasks.shadowJar.get().archiveFile)
}