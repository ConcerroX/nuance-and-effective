package concerrox.effective.neoforge

import concerrox.effective.registry.ModAmbientConditions
import concerrox.effective.registry.ModAmbientConditions.AmbientCondition
import concerrox.effective.registry.ModSounds
import concerrox.effective.util.isInCave
import concerrox.effective.util.isInOverworld
import concerrox.effective.util.isNightTime
import net.minecraft.tags.BiomeTags
import net.neoforged.neoforge.common.Tags.Biomes

typealias BiomeRegistry = net.minecraft.world.level.biome.Biomes

object ModAmbientConditionsForge {

    fun initialize() {
        val conditions = ModAmbientConditions.CONDITIONS

        // TODO: Fix floral and temperate tags
        conditions.add(AmbientCondition(ModSounds.AMBIENT_ANIMAL_BEES, AmbientCondition.Type.ANIMAL) { level, pos, _ ->
            level.isInOverworld(pos) && !level.isInCave(pos) && level.getBiome(pos)
                .`is`(BiomeRegistry.FLOWER_FOREST) && !level.isNightTime
        })

        // birds in forests during the day
        conditions.add(AmbientCondition(ModSounds.AMBIENT_ANIMAL_BIRDS, AmbientCondition.Type.ANIMAL) { level, pos, _ ->
            level.isInOverworld(pos) && !level.isInCave(pos) && level.getBiome(pos)
                .`is`(BiomeTags.IS_FOREST) && !level.isNightTime
        })

        // cicadas in savannas during day
        conditions.add(
            AmbientCondition(ModSounds.AMBIENT_ANIMAL_CICADAS, AmbientCondition.Type.ANIMAL) { level, pos, _ ->
                level.isInOverworld(pos) && !level.isInCave(pos) && level.getBiome(pos)
                    .`is`(BiomeTags.IS_SAVANNA) && !level.isNightTime
            })

        // crickets in temperate (excluding swamps to use their dedicated cricket and frog ambience instead) and floral biomes at night
        conditions.add(
            AmbientCondition(ModSounds.AMBIENT_ANIMAL_CRICKETS, AmbientCondition.Type.ANIMAL) { level, pos, _ ->
                level.isInOverworld(pos) && !level.isInCave(pos) && ((
                    level.getBiome(pos).`is`(BiomeTags.IS_FOREST) &&
                        !level.getBiome(pos).`is`(Biomes.IS_SWAMP)) || level.getBiome(pos)
                    .`is`(BiomeRegistry.FLOWER_FOREST)) && level.isNightTime
            })

        // frogs and crickets in swamps at night
        conditions.add(AmbientCondition(ModSounds.AMBIENT_ANIMAL_FROGS_AND_CRICKETS,
            AmbientCondition.Type.ANIMAL) { level, pos, _ ->
            level.isInOverworld(pos) && !level.isInCave(pos) && (level.getBiome(pos)
                .`is`(Biomes.IS_SWAMP)) && level.isNightTime
        })

        // day jungle animals in jungles during the day
        conditions.add(
            AmbientCondition(ModSounds.AMBIENT_ANIMAL_JUNGLE_DAY, AmbientCondition.Type.ANIMAL) { level, pos, _ ->
                level.isInOverworld(pos) && !level.isInCave(pos) && (level.getBiome(pos)
                    .`is`(BiomeTags.IS_JUNGLE)) && !level.isNightTime
            })

        // night jungle animals in jungles at night
        conditions.add(
            AmbientCondition(ModSounds.AMBIENT_ANIMAL_JUNGLE_NIGHT, AmbientCondition.Type.ANIMAL) { level, pos, _ ->
                level.isInOverworld(pos) && !level.isInCave(pos) && (level.getBiome(pos)
                    .`is`(BiomeTags.IS_JUNGLE)) && level.isNightTime
            })

        // mangrove birds in mangroves during the day
        conditions.add(
            AmbientCondition(ModSounds.AMBIENT_ANIMAL_MANGROVE_BIRDS, AmbientCondition.Type.ANIMAL) { level, pos, _ ->
                level.isInOverworld(pos) && !level.isInCave(pos) && level.getBiome(pos)
                    .`is`(BiomeRegistry.MANGROVE_SWAMP) && !level.isNightTime
            })

        // owls in forests at night
        conditions.add(AmbientCondition(ModSounds.AMBIENT_ANIMAL_OWLS, AmbientCondition.Type.ANIMAL) { level, pos, _ ->
            level.isInOverworld(pos) && !level.isInCave(pos) && (level.getBiome(pos)
                .`is`(BiomeTags.IS_FOREST)) && level.isNightTime
        })

        // rustling reverbed foliage in lush caves
        conditions.add(
            AmbientCondition(ModSounds.AMBIENT_FOLIAGE_CAVE_LEAVES, AmbientCondition.Type.FOLIAGE) { level, pos, _ ->
                level.isInOverworld(pos) && level.getBiome(pos).`is`(BiomeRegistry.LUSH_CAVES)
            })

        // rustling foliage in forests, floral biomes, swamps, jungles, wooded badlands and lush caves
        conditions.add(
            AmbientCondition(ModSounds.AMBIENT_FOLIAGE_LEAVES, AmbientCondition.Type.FOLIAGE) { level, pos, _ ->
                level.isInOverworld(pos) && !level.isInCave(pos) && (level.getBiome(pos)
                    .`is`(BiomeTags.IS_FOREST) || level.getBiome(pos)
                    .`is`(BiomeRegistry.FLOWER_FOREST) || level.getBiome(pos)
                    .`is`(Biomes.IS_SWAMP) || level.getBiome(pos)
                    .`is`(BiomeTags.IS_JUNGLE) || level.getBiome(pos).`is`(BiomeRegistry.WOODED_BADLANDS))
            })

        // water dripping in dripstone caves
        conditions.add(AmbientCondition(ModSounds.AMBIENT_WATER_DRIPSTONE_CAVES,
            AmbientCondition.Type.WATER) label@{ level, pos, _ ->
            if (level.isInOverworld(pos)) {
                if (level.isInCave(pos)) {
                    val mutable = pos.mutable()
                    val startY = mutable.y
                    var y = startY
                    while (y <= startY + 20) {
                        mutable.setY(y)
                        if (level.getBiome(mutable).`is`(BiomeRegistry.DRIPSTONE_CAVES)) {
                            return@label true
                        }
                        y += 5
                    }
                    return@label false
                } else return@label level.getBiome(pos).`is`(BiomeRegistry.DRIPSTONE_CAVES)
            } else return@label false
        })

        // water streams in lush caves
        conditions.add(
            AmbientCondition(ModSounds.AMBIENT_WATER_LUSH_CAVES, AmbientCondition.Type.WATER) { level, pos, _ ->
                level.isInOverworld(pos) && level.getBiome(pos).`is`(BiomeRegistry.LUSH_CAVES)
            })

        // water flowing in rivers
        conditions.add(AmbientCondition(ModSounds.AMBIENT_WATER_RIVER, AmbientCondition.Type.WATER) { level, pos, _ ->
            level.isInOverworld(pos) && !level.isInCave(pos) && level.getBiome(pos).`is`(BiomeTags.IS_RIVER)
        })

        // water waves in beaches and oceans
        conditions.add(AmbientCondition(ModSounds.AMBIENT_WATER_WAVES, AmbientCondition.Type.WATER) { level, pos, _ ->
            level.isInOverworld(pos) && !level.isInCave(pos) && level.getBiome(pos)
                .`is`(BiomeTags.IS_BEACH) || level.getBiome(pos).`is`(BiomeTags.IS_OCEAN)
        })

        // arid wind in deserts and mesas
        conditions.add(AmbientCondition(ModSounds.AMBIENT_WIND_ARID, AmbientCondition.Type.WIND) { level, pos, _ ->
            level.isInOverworld(pos) && !level.isInCave(pos) && (level.getBiome(pos)
                .`is`(Biomes.IS_DESERT) || level.getBiome(pos).`is`(BiomeTags.IS_BADLANDS))
        })

        // cave wind in caves (excluding the deep dark to use its dedicated ambience instead)
        conditions.add(AmbientCondition(ModSounds.AMBIENT_WIND_CAVE, AmbientCondition.Type.WIND) { level, pos, _ ->
            level.isInOverworld(pos) && level.isInCave(pos) && !level.getBiome(pos).`is`(BiomeRegistry.DEEP_DARK)
        })

        // cold wind in cold biomes (excluding peaks to use their dedicated wind instead) and mountain slopes
        conditions.add(AmbientCondition(ModSounds.AMBIENT_WIND_COLD, AmbientCondition.Type.WIND) { level, pos, _ ->
            level.isInOverworld(pos) && !level.isInCave(pos) && (level.getBiome(pos)
                .`is`(Biomes.IS_COLD_OVERWORLD) || level.getBiome(pos)
                .`is`(Biomes.IS_MOUNTAIN_SLOPE)) && !level.getBiome(pos)
                .`is`(Biomes.IS_MOUNTAIN_PEAK)
        })

        // deep dark ambience (classified as wind)
        conditions.add(AmbientCondition(ModSounds.AMBIENT_WIND_DEEP_DARK, AmbientCondition.Type.WIND) { level, pos, _ ->
            level.isInOverworld(pos) && level.getBiome(pos).`is`(BiomeRegistry.DEEP_DARK)
        })

        // end ambience (classified as wind)
        conditions.add(AmbientCondition(ModSounds.AMBIENT_WIND_END, AmbientCondition.Type.WIND) { level, pos, _ ->
            level.getBiome(pos).`is`(BiomeTags.IS_END)
        })

        // mountain wind in peaks
        conditions.add(AmbientCondition(ModSounds.AMBIENT_WIND_MOUNTAINS, AmbientCondition.Type.WIND) { level, pos, _ ->
            level.isInOverworld(pos) && !level.isInCave(pos) && level.getBiome(pos)
                .`is`(Biomes.IS_MOUNTAIN_PEAK)
        })

        // soft wind in temperate, floral, savanna, jungle, swamp and mushroom field biomes
        conditions.add(AmbientCondition(ModSounds.AMBIENT_WIND_TEMPERATE, AmbientCondition.Type.WIND) { level, pos, _ ->
            level.isInOverworld(pos) && !level.isInCave(pos) && (level.getBiome(pos)
                .`is`(BiomeTags.IS_FOREST) || level.getBiome(pos)
                .`is`(BiomeRegistry.FLOWER_FOREST) || level.getBiome(pos).`is`(BiomeTags.IS_SAVANNA) || level.getBiome(
                pos).`is`(BiomeTags.IS_JUNGLE) || level.getBiome(pos)
                .`is`(Biomes.IS_SWAMP) || level.getBiome(pos).`is`(Biomes.IS_MUSHROOM))
        })
    }

}