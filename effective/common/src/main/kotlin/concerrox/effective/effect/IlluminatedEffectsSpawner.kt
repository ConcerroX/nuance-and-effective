package concerrox.effective.effect

import com.mojang.logging.LogUtils
import concerrox.effective.EffectiveConfig
import concerrox.effective.registry.ModParticles
import concerrox.effective.util.isNightTime
import concerrox.effective.util.nextDoubleOrNegative
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.biome.Biome
import concerrox.effective.particle.FireflyParticle
import java.awt.Color

object IlluminatedEffectsSpawner {

    internal val fireflySpawnSettings by lazy {
        EffectiveConfig.fireflySpawnSettings.get().associate {
            val s = FireflySpawnSetting.tryParse(it)
            s?.biome to s
        }
    }

    fun trySpawnFireflies(level: Level, blockPos: BlockPos.MutableBlockPos, random: RandomSource) {
        if (level.isNightTime) {
            val pos = blockPos.offset((random.nextDoubleOrNegative() * 50).toInt(),
                (random.nextDoubleOrNegative() * 10).toInt(), (random.nextDoubleOrNegative() * 50).toInt()).mutable()
            val pos2 = pos.mutable()
            val biome = level.getBiome(pos)
            fireflySpawnSettings[biome.unwrapKey().get()]?.let {
                if (random.nextFloat() * 500F <= it.spawnChance * EffectiveConfig.fireflyDensity.get() && pos.y > level.seaLevel) {
                    for (y in level.seaLevel..level.seaLevel * 2) {
                        pos.setY(y)
                        pos2.setY(y - 1)
                        val canSpawnFirefly = FireflyParticle.canFlyThroughBlock(level, pos,
                            level.getBlockState(pos)) && !FireflyParticle.canFlyThroughBlock(level, pos2,
                            level.getBlockState(pos2))
                        if (canSpawnFirefly) {
                            level.addParticle(ModParticles.FIREFLY, (pos.x + random.nextFloat()).toDouble(),
                                (pos.y + random.nextFloat() * 5f).toDouble(), (pos.z + random.nextFloat()).toDouble(),
                                0.0, 0.0, 0.0)
                            break
                        }
                    }
                }
            }
        }
    }

    data class FireflySpawnSetting(val biome: ResourceKey<Biome>, val spawnChance: Float, val color: Color) {

        companion object {

            fun tryParse(str: String): FireflySpawnSetting? {
                val data = str.split(",", ignoreCase = false, limit = 3)
                if (data.size != 3) return null
                val id = ResourceLocation.tryParse(data[0]) ?: return null
                val chance = tryParseChance(data[1]) ?: return null
                val color = tryParseColor(data[2]) ?: return null
                return FireflySpawnSetting(ResourceKey.create(Registries.BIOME, id), chance, color)
            }

            private fun tryParseColor(hexColor: String): Color? {
                try {
                    return Color(hexColor.substring(1).toInt(16))
                } catch (e: NumberFormatException) {
                    LogUtils.getLogger().error("Invalid firefly color format: $hexColor")
                    return null
                }
            }

            private fun tryParseChance(chance: String): Float? {
                return when (chance) {
                    "LOW" -> 0.01F
                    "MEDIUM" -> 0.05F
                    "HIGH" -> 0.1F
                    else -> chance.toFloatOrNull()
                }
            }

        }

    }

}