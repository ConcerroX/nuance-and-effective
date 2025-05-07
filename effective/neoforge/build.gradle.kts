import org.gradle.internal.extensions.core.extra

val neoForgeVersion: String by rootProject
val mixinExtrasVersion: String by rootProject
val kotlinForNeoForgeVersion: String by rootProject
val configuredForgeVersion: String by rootProject
val velvetVersion: String by rootProject
val sparkweaveVersion: String by rootProject

plugins {
    id("com.gradleup.shadow")
}

architectury {
    platformSetupLoomIde()
    neoForge()
}

loom {
    accessWidenerPath = project(":effective:common").loom.accessWidenerPath
}

val common: Configuration by configurations.creating
val shadowCommon: Configuration by configurations.creating
val developmentNeoForge: Configuration by configurations.getting

configurations {
    compileOnly.configure { extendsFrom(common) }
    runtimeOnly.configure { extendsFrom(common) }
    developmentNeoForge.extendsFrom(common)
}

repositories {
    maven("https://maven.neoforged.net/releases/")
    maven("https://thedarkcolour.github.io/KotlinForForge/")
    maven("https://repo.constructlegacy.ru/public")
    maven("https://jm.gserv.me/repository/maven-public/")
}

dependencies {
    neoForge("net.neoforged:neoforge:$neoForgeVersion")
    implementation("thedarkcolour:kotlinforforge-neoforge:$kotlinForNeoForgeVersion") {
        exclude(group = "net.neoforged.fancymodloader", module = "loader")
    }
//    compileOnly(annotationProcessor("io.github.llamalad7:mixinextras-common:$mixinExtrasVersion")!!)
//    implementation(include("io.github.llamalad7:mixinextras-forge:$mixinExtrasVersion")!!)

    modLocalRuntime("curse.maven:configured-457570:$configuredForgeVersion")
    implementation("dev.upcraft.sparkweave:Sparkweave-NeoForge:$sparkweaveVersion")
    implementation("dev.cammiescorner.velvet:Velvet-NeoForge:$velvetVersion")

    common(project(":effective:common", "namedElements")) { isTransitive = false }
    shadowCommon(project(":effective:common", "transformProductionNeoForge")) { isTransitive = false }
}

tasks.processResources {
    inputs.property("version", project.version)
    filesMatching("META-INF/neoforge.mods.toml") {
        expand(mapOf("name" to rootProject.extra["effectiveModName"], "version" to project.extra["effectiveModVersion"],
            "description" to project.description))
    }
}

tasks.shadowJar {
    configurations = listOf(shadowCommon)
    archiveClassifier.set("dev-shadow")
}

tasks.remapJar {
    inputFile.set(tasks.shadowJar.get().archiveFile)
}