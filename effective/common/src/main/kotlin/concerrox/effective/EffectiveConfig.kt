package concerrox.effective

import concerrox.effective.util.ForgeConfigSpec
import concerrox.effective.util.category
import net.minecraftforge.common.ForgeConfigSpec

object EffectiveConfig {

    private val DEFAULT_FIREFLY_SPAWN_SETTINGS = listOf("minecraft:plains,LOW,#91BD59",
        "minecraft:sunflower_plains,LOW,#91BD59", "minecraft:old_growth_pine_taiga,LOW,#BFFF00",
        "minecraft:old_growth_spruce_taiga,LOW,#BFFF00", "minecraft:taiga,LOW,#BFFF00",
        "minecraft:forest,MEDIUM,#BFFF00", "minecraft:flower_forest,MEDIUM,#FF7FED",
        "minecraft:birch_forest,MEDIUM,#E4FF00", "minecraft:dark_forest,MEDIUM,#006900",
        "minecraft:old_growth_birch_forest,MEDIUM,#E4FF00", "minecraft:jungle,MEDIUM,#00FF21",
        "minecraft:sparse_jungle,MEDIUM,#00FF21", "minecraft:bamboo_jungle,MEDIUM,#00FF21",
        "minecraft:lush_caves,MEDIUM,#F2B646", "minecraft:swamp,HIGH,#BFFF00", "minecraft:mangrove_swamp,HIGH,#BFFF00",
        "mariposa:redwood_forest,MEDIUM,#BFFF00")

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
                underwaterChestsOpenRandomly = defineEnum("underwaterChestsOpenRandomly",
                    ChestsOpenOptions.ON_SOUL_SAND)
            }
            category("entityEffects") {
                glowSquidHypnosis = defineEnum("glowSquidHypnosis", GlowSquidHypnosisOptions.ATTRACT)
                allayTrails = defineEnum("allayTrails", TrailOptions.BOTH)
                goldenAllays = define("goldenAllays", true)
            }
            category("screenShakeEffects") {
                screenShakeIntensity = defineInRange("screenShakeIntensity", 1.0, 0.0, 5.0)
                wardenScreenShake = define("wardenScreenShake", true)
                sonicBoomScreenShake = define("sonicBoomScreenShake", true)
                ravagerScreenShake = define("ravagerScreenShake", true)
                dragonScreenShake = define("dragonScreenShake", true)
            }
            category("illuminatedEffects") {
                fireflyDensity = defineInRange("fireflyDensity", 1.0, 0.0, 10.0)
                fireflySpawnSettings = defineList("fireflySpawnSettings", DEFAULT_FIREFLY_SPAWN_SETTINGS) { it is String }
                chorusPetalDensity = defineInRange("chorusPetalDensity", 1.0, 0.0, 10.0)
                willOWispDensity = defineInRange("willOWispDensity", 1.0, 0.0, 1000.0)
                eyesInTheDark = defineEnum("eyesInTheDark", EyesInTheDarkOptions.HALLOWEEN)
            }
            category("improvementEffects") {
                spectralArrowTrails = defineEnum("spectralArrowTrails", TrailOptions.BOTH)
            }
            category("miscellaneousEffects") {
                sculkDustDensity = defineInRange("sculkDustDensity", 100, 0, 100)
            }
        }

        category("audio") {
            category("cascadeAudio") {
                cascadeSoundsVolume = defineInRange("cascadeSoundsVolume", 30, 0, 100)
                cascadeSoundDistanceBlocks = defineInRange("cascadeSoundDistanceBlocks", 100, 0, 400)
            }
            category("biomeAmbienceAudio") {
                windAmbienceVolume = defineInRange("windAmbienceVolume", 100, 0, 100)
                waterAmbienceVolume = defineInRange("waterAmbienceVolume", 100, 0, 100)
                foliageAmbienceVolume = defineInRange("foliageAmbienceVolume", 100, 0, 100)
                animalAmbienceVolume = defineInRange("animalAmbienceVolume", 100, 0, 100)
            }
        }

        category("dynamicLights") {

        }

        category("technical") {
            randomBlockDisplayTicksFrequencyMultiplier = defineInRange("randomBlockDisplayTicksFrequencyMultiplier", 1.0, 0.0, 25.0)
            randomBlockDisplayTicksDistanceClose = defineInRange("randomBlockDisplayTicksDistanceClose", 16, 0, 160)
            randomBlockDisplayTicksDistanceFar = defineInRange("randomBlockDisplayTicksDistanceFar", 32, 0, 320)
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
    lateinit var underwaterChestsOpenRandomly: ForgeConfigSpec.EnumValue<ChestsOpenOptions>

    lateinit var glowSquidHypnosis: ForgeConfigSpec.EnumValue<GlowSquidHypnosisOptions>
    lateinit var allayTrails: ForgeConfigSpec.EnumValue<TrailOptions>
    lateinit var goldenAllays: ForgeConfigSpec.BooleanValue

    lateinit var screenShakeIntensity: ForgeConfigSpec.DoubleValue
    lateinit var wardenScreenShake: ForgeConfigSpec.BooleanValue
    lateinit var sonicBoomScreenShake: ForgeConfigSpec.BooleanValue
    lateinit var ravagerScreenShake: ForgeConfigSpec.BooleanValue
    lateinit var dragonScreenShake: ForgeConfigSpec.BooleanValue

    lateinit var fireflyDensity: ForgeConfigSpec.DoubleValue
    lateinit var fireflySpawnSettings: ForgeConfigSpec.ConfigValue<List<String>>
    lateinit var chorusPetalDensity: ForgeConfigSpec.DoubleValue
    lateinit var willOWispDensity: ForgeConfigSpec.DoubleValue
    lateinit var eyesInTheDark: ForgeConfigSpec.EnumValue<EyesInTheDarkOptions>

    lateinit var spectralArrowTrails: ForgeConfigSpec.EnumValue<TrailOptions>

    lateinit var sculkDustDensity: ForgeConfigSpec.IntValue

    lateinit var cascadeSoundsVolume: ForgeConfigSpec.IntValue
    lateinit var cascadeSoundDistanceBlocks: ForgeConfigSpec.IntValue

    lateinit var windAmbienceVolume: ForgeConfigSpec.IntValue
    lateinit var waterAmbienceVolume: ForgeConfigSpec.IntValue
    lateinit var foliageAmbienceVolume: ForgeConfigSpec.IntValue
    lateinit var animalAmbienceVolume: ForgeConfigSpec.IntValue

    lateinit var randomBlockDisplayTicksFrequencyMultiplier: ForgeConfigSpec.DoubleValue
    lateinit var randomBlockDisplayTicksDistanceClose: ForgeConfigSpec.IntValue
    lateinit var randomBlockDisplayTicksDistanceFar: ForgeConfigSpec.IntValue

    enum class ChestsOpenOptions {
        ON_SOUL_SAND, RANDOMLY, NEVER
    }

    enum class GlowSquidHypnosisOptions {
        ATTRACT, VISUAL, NEVER
    }

    enum class TrailOptions {
        BOTH, TRAIL, TWINKLE, NONE
    }

    enum class EyesInTheDarkOptions {
        HALLOWEEN, ALWAYS, NEVER
    }

    fun canGlowSquidsHypnotize(): Boolean {
        return glowSquidHypnosis.get() == GlowSquidHypnosisOptions.ATTRACT || glowSquidHypnosis.get() == GlowSquidHypnosisOptions.VISUAL
    }
}
