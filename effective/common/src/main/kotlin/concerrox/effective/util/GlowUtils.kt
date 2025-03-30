package concerrox.effective.util

import concerrox.effective.EffectiveConfig
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.LightLayer
import net.minecraft.world.level.biome.Biomes
import java.awt.Color
import kotlin.math.min

object GlowUtils {

    private val Level.isNightTime
        get() = getSunAngle(dayTime.toFloat()) >= 0.25965086 && getTimeOfDay(dayTime.toFloat()) <= 0.7403491

    fun isGlowingWater(level: Level, pos: BlockPos): Boolean {
        return EffectiveConfig.glowingPlankton.get() && level.isNightTime && level.getBiome(pos).`is`(Biomes.WARM_OCEAN)
    }

    fun getGlowingWaterColor(level: Level, pos: BlockPos): Color {
        return Color(
            min(1F, level.random.nextFloat() / 5F + level.getBrightness(LightLayer.BLOCK, pos) / 15F),
            min(1F, level.random.nextFloat() / 5F + level.getBrightness(LightLayer.BLOCK, pos) / 15F), 1F,
        )
    }
}
