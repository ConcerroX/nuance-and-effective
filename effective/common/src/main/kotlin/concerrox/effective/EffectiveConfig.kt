package concerrox.effective

import concerrox.effective.util.ModConfigSpec
import concerrox.effective.util.category
import net.neoforged.neoforge.common.ModConfigSpec

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

    val configSpec = ModConfigSpec {
        category("visuals") {
            category("waterEffects") {
                isSplashesEnabled = define("isSplashesEnabled", true)
                isLavaSplashesEnabled = define("isLavaSplashesEnabled", false)

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

    lateinit var isSplashesEnabled: ModConfigSpec.BooleanValue
    lateinit var isLavaSplashesEnabled: ModConfigSpec.BooleanValue
    lateinit var splashThreshold: ModConfigSpec.DoubleValue

    lateinit var dropletSplashingDensity: ModConfigSpec.IntValue

    lateinit var isCascadesEnabled: ModConfigSpec.BooleanValue
    lateinit var cascadeCloudDensity: ModConfigSpec.IntValue
    lateinit var cascadeMistDensity: ModConfigSpec.DoubleValue
    lateinit var flowingWaterSpawnCascade: ModConfigSpec.BooleanValue
    lateinit var lapisBlockUpdateParticleChance: ModConfigSpec.DoubleValue

    lateinit var rainRippleDensity: ModConfigSpec.IntValue
    lateinit var isGlowingPlanktonEnabled: ModConfigSpec.BooleanValue

    lateinit var underwaterOpenChestBubbles: ModConfigSpec.BooleanValue
    lateinit var underwaterChestsOpenRandomly: ModConfigSpec.EnumValue<ChestsOpenOptions>

    lateinit var glowSquidHypnosis: ModConfigSpec.EnumValue<GlowSquidHypnosisOptions>
    lateinit var allayTrails: ModConfigSpec.EnumValue<TrailOptions>
    lateinit var goldenAllays: ModConfigSpec.BooleanValue

    lateinit var screenShakeIntensity: ModConfigSpec.DoubleValue
    lateinit var wardenScreenShake: ModConfigSpec.BooleanValue
    lateinit var sonicBoomScreenShake: ModConfigSpec.BooleanValue
    lateinit var ravagerScreenShake: ModConfigSpec.BooleanValue
    lateinit var dragonScreenShake: ModConfigSpec.BooleanValue

    lateinit var fireflyDensity: ModConfigSpec.DoubleValue
    lateinit var fireflySpawnSettings: ModConfigSpec.ConfigValue<List<String>>
    lateinit var chorusPetalDensity: ModConfigSpec.DoubleValue
    lateinit var willOWispDensity: ModConfigSpec.DoubleValue
    lateinit var eyesInTheDark: ModConfigSpec.EnumValue<EyesInTheDarkOptions>

    lateinit var spectralArrowTrails: ModConfigSpec.EnumValue<TrailOptions>

    lateinit var sculkDustDensity: ModConfigSpec.IntValue

    lateinit var cascadeSoundsVolume: ModConfigSpec.IntValue
    lateinit var cascadeSoundDistanceBlocks: ModConfigSpec.IntValue

    lateinit var windAmbienceVolume: ModConfigSpec.IntValue
    lateinit var waterAmbienceVolume: ModConfigSpec.IntValue
    lateinit var foliageAmbienceVolume: ModConfigSpec.IntValue
    lateinit var animalAmbienceVolume: ModConfigSpec.IntValue

    lateinit var randomBlockDisplayTicksFrequencyMultiplier: ModConfigSpec.DoubleValue
    lateinit var randomBlockDisplayTicksDistanceClose: ModConfigSpec.IntValue
    lateinit var randomBlockDisplayTicksDistanceFar: ModConfigSpec.IntValue

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
