package concerrox.effective

import net.minecraftforge.common.ForgeConfigSpec

private fun ForgeConfigSpec.Builder.category(
    path: String,
    action: ForgeConfigSpec.Builder.() -> Unit,
) {
    push(path)
    action(this)
    pop()
}

private fun ForgeConfigSpec(action: ForgeConfigSpec.Builder.() -> Unit): ForgeConfigSpec {
    val builder = ForgeConfigSpec.Builder()
    action(builder)
    return builder.build()
}

object EffectiveConfig {
    val configSpec =
        ForgeConfigSpec {
            category("visuals") {
                isCascadeEnabled = define("isCascadeEnabled", true)
                cascadeCloudDensity = defineInRange("cascadeCloudDensity", 2, 0, 5)
                cascadeMistDensity = defineInRange("cascadeMistDensity", 1.0, 0.0, 5.0)
                flowingWaterSpawnCascade = define("flowingWaterSpawnCascade", true)
                lapisBlockUpdateParticleChance = defineInRange("lapisBlockUpdateParticleChance", 1.0, 0.0, 10.0)
                glowingPlankton = define("glowingPlankton", true)
            }
            category("audio") {
                cascadeSoundsVolume = defineInRange("cascadeSoundsVolume", 30, 0, 100)
                cascadeSoundDistanceBlocks = defineInRange("cascadeSoundDistanceBlocks", 100, 0, 400)
            }
        }

    lateinit var isCascadeEnabled: ForgeConfigSpec.BooleanValue
    lateinit var cascadeCloudDensity: ForgeConfigSpec.IntValue
    lateinit var cascadeMistDensity: ForgeConfigSpec.DoubleValue
    lateinit var flowingWaterSpawnCascade: ForgeConfigSpec.BooleanValue
    lateinit var lapisBlockUpdateParticleChance: ForgeConfigSpec.DoubleValue
    lateinit var glowingPlankton: ForgeConfigSpec.BooleanValue

    lateinit var cascadeSoundsVolume: ForgeConfigSpec.IntValue
    lateinit var cascadeSoundDistanceBlocks: ForgeConfigSpec.IntValue
}
