package concerrox.effective.util

import concerrox.effective.EffectiveConfig
import concerrox.effective.registry.ModParticles
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.LightLayer
import net.minecraft.world.level.biome.Biomes
import net.minecraft.world.phys.Vec3
import java.awt.Color
import kotlin.math.min


object WaterUtils {

    enum class WaterEffectType {
        DROPLET, RIPPLE,
    }

    private val Level.isNightTime
        get() = getSunAngle(dayTime.toFloat()) >= 0.25965086 && getTimeOfDay(dayTime.toFloat()) <= 0.7403491

    fun isGlowingWater(level: Level, pos: BlockPos): Boolean {
        return EffectiveConfig.isGlowingPlanktonEnabled.get() && level.isNightTime && level.getBiome(pos)
            .`is`(Biomes.WARM_OCEAN)
    }

    fun getGlowingWaterColor(level: Level, pos: BlockPos): Color {
        return Color(
            min(1F, level.random.nextFloat() / 5F + level.getBrightness(LightLayer.BLOCK, pos) / 15F),
            min(1F, level.random.nextFloat() / 5F + level.getBrightness(LightLayer.BLOCK, pos) / 15F), 1F,
        )
    }

    fun spawnWaterEffect(
        level: Level, pos: Vec3, velocityX: Double, velocityY: Double, velocityZ: Double, waterEffect: WaterEffectType
    ) {
        var particle = when (waterEffect) {
            WaterEffectType.DROPLET -> ModParticles.DROPLET
            WaterEffectType.RIPPLE -> ModParticles.RIPPLE
        }
        if (isGlowingWater(level, BlockPos.containing(pos))) {
            particle = when (waterEffect) {
                WaterEffectType.DROPLET -> ModParticles.GLOWING_DROPLET
                WaterEffectType.RIPPLE -> ModParticles.GLOWING_RIPPLE
            }
        }
        level.addParticle(particle, pos.x, pos.y, pos.z, velocityX, velocityY, velocityZ)
    }

}
