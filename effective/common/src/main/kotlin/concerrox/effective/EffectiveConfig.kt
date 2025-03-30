package concerrox.effective

import concerrox.effective.util.ForgeConfigSpec
import concerrox.effective.util.category
import net.minecraftforge.common.ForgeConfigSpec

object EffectiveConfig {
    val configSpec = ForgeConfigSpec {
        category("visuals") {
            category("waterEffects") {
                isSplashesEnabled = define("isSplashesEnabled", true)

                splashThreshold = defineInRange("splashThreshold", 0.3, 0.0, 5.0)
                dropletSplashingDensity = defineInRange("dropletSplashingDensity", 50, 0, 100)

                isCascadesEnabled = define("isCascadesEnabled", true)
                cascadeCloudDensity = defineInRange("cascadeCloudDensity", 2, 0, 5)
                cascadeMistDensity = defineInRange("cascadeMistDensity", 1.0, 0.0, 5.0)
                flowingWaterSpawnCascade = define("flowingWaterSpawnCascade", true)
                lapisBlockUpdateParticleChance = defineInRange("lapisBlockUpdateParticleChance", 1.0, 0.0, 10.0)

                rainRippleDensity = defineInRange("rainRippleDensity", 1, 0, 10)
                isGlowingPlanktonEnabled = define("isGlowingPlanktonEnabled", true)
                underwaterOpenChestBubbles = define("underwaterOpenChestBubbles", true)
//                underwaterChestsOpenRandomly
            }
        }
        category("audio") {
            cascadeSoundsVolume = defineInRange("cascadeSoundsVolume", 30, 0, 100)
            cascadeSoundDistanceBlocks = defineInRange("cascadeSoundDistanceBlocks", 100, 0, 400)
        }
    }

    lateinit var isSplashesEnabled: ForgeConfigSpec.BooleanValue
    lateinit var splashThreshold: ForgeConfigSpec.DoubleValue

    lateinit var dropletSplashingDensity: ForgeConfigSpec.IntValue

    lateinit var isCascadesEnabled: ForgeConfigSpec.BooleanValue
    lateinit var cascadeCloudDensity: ForgeConfigSpec.IntValue
    lateinit var cascadeMistDensity: ForgeConfigSpec.DoubleValue
    lateinit var flowingWaterSpawnCascade: ForgeConfigSpec.BooleanValue
    lateinit var lapisBlockUpdateParticleChance: ForgeConfigSpec.DoubleValue

    lateinit var rainRippleDensity: ForgeConfigSpec.IntValue
    lateinit var isGlowingPlanktonEnabled: ForgeConfigSpec.BooleanValue
    lateinit var underwaterOpenChestBubbles: ForgeConfigSpec.BooleanValue

    lateinit var cascadeSoundsVolume: ForgeConfigSpec.IntValue
    lateinit var cascadeSoundDistanceBlocks: ForgeConfigSpec.IntValue
}
