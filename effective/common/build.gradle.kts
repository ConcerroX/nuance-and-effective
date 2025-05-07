val enabledPlatforms: String by rootProject
val fabricLoaderVersion: String by rootProject
val forgeConfigApiPortVersion: String by rootProject
val effectiveModId: String by rootProject
val velvetVersion: String by rootProject

architectury {
    common(enabledPlatforms.split(","))
}

loom {
    accessWidenerPath = file("src/main/resources/$effectiveModId.accesswidener")
}

dependencies {
    // We depend on Fabric Loader here to use the Fabric @Environment annotations,
    // which get remapped to the correct annotations on each platform.
    // Do NOT use other classes from Fabric Loader.
    modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
    modApi("fuzs.forgeconfigapiport:forgeconfigapiport-common-neoforgeapi:$forgeConfigApiPortVersion")
    modCompileOnly("dev.cammiescorner.velvet:Velvet-Common:$velvetVersion")
}