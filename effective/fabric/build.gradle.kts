import org.gradle.internal.extensions.core.extra

val fabricLoaderVersion: String by rootProject

plugins {
    id("com.github.johnrengelman.shadow")
}

architectury {
    platformSetupLoomIde()
    fabric()
}

val common: Configuration by configurations.creating
val shadowCommon: Configuration by configurations.creating
val developmentFabric: Configuration by configurations.getting
configurations {
    compileOnly.configure { extendsFrom(common) }
    runtimeOnly.configure { extendsFrom(common) }
    developmentFabric.extendsFrom(common)
}

dependencies {
    modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
    common(project(":effective:common", "namedElements")) { isTransitive = false }
    shadowCommon(project(":effective:common", "transformProductionFabric")) { isTransitive = false }
}

tasks.processResources {
//    inputs.property("version", project.version)
//    inputs.property("description", project.description)
//    inputs.property("name", rootProject.extra["effectiveModName"])
    filesMatching("fabric.mod.json") {
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
