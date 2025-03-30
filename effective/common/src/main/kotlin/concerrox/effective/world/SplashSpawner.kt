package concerrox.effective.world

import concerrox.effective.EffectiveConfig
import concerrox.effective.util.nextDoubleOrNegative
import concerrox.effective.particle.type.SplashParticleType.SplashParticleInitialData
import concerrox.effective.registry.ModParticles
import concerrox.effective.util.WaterUtils
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.FishingHook
import net.minecraft.world.level.Level
import net.minecraft.world.level.material.Fluids
import net.minecraft.world.phys.Vec3
import kotlin.math.min
import kotlin.math.roundToInt
import kotlin.math.sqrt


object SplashSpawner {

    fun trySpawnSplash(entity: Entity) {
        val topMostEntity = if (entity.isVehicle && entity.firstPassenger != null) entity.firstPassenger else entity
        if (topMostEntity !is FishingHook && topMostEntity != null) {
            val amplifier = if (topMostEntity === entity) 0.2F else 0.9F
            val impactVelocity = topMostEntity.deltaMovement
            if (impactVelocity.length() < EffectiveConfig.splashThreshold.get()) {
                return
            }
            val splashIntensity = min(1F, computeSplashIntensity(impactVelocity, amplifier))
            for (y in -10..9) {
                if (isValidSplashPosition(entity, y)) {
                    val splashY = entity.y.roundToInt() + y + 0.9F
                    entity.level().playLocalSound(entity.x, splashY.toDouble(), entity.z,
                        if (topMostEntity is Player) SoundEvents.PLAYER_SPLASH else SoundEvents.GENERIC_SPLASH,
                        SoundSource.AMBIENT, splashIntensity * 10F, 0.8F, true)
                    val data = SplashParticleInitialData(topMostEntity.bbWidth, impactVelocity.y)
                    spawnSplash(entity.level(), entity.x, splashY.toDouble(), entity.z, data)
                    break
                }
            }
            spawnWaterEffects(entity)
        }
    }

    private fun computeSplashIntensity(impactVelocity: Vec3, f: Float): Float {
        return sqrt(
            impactVelocity.x * impactVelocity.x * 0.2 + impactVelocity.y * impactVelocity.y + impactVelocity.z * impactVelocity.z * 0.2).toFloat() * f
    }

    private fun spawnWaterEffects(entity: Entity) {
        val random = entity.level().getRandom()
        for (j in 0 until (entity.bbWidth * 25).toInt()) {
            WaterUtils.spawnWaterEffect(level = entity.level(),
                pos = Vec3(entity.x + random.nextDoubleOrNegative() * entity.bbWidth / 5F, entity.y,
                    entity.z + random.nextDoubleOrNegative() * entity.bbWidth),
                velocityX = random.nextDoubleOrNegative() / 15.0, velocityY = random.nextDouble() / 2.5,
                velocityZ = random.nextDoubleOrNegative() / 15.0, waterEffect = WaterUtils.WaterEffectType.DROPLET)
        }
    }

    private fun isValidSplashPosition(entity: Entity, yOffset: Int): Boolean {
        val pos = BlockPos.containing(entity.x, (entity.y.roundToInt() + yOffset).toDouble(), entity.z)
        val blockState = entity.level().getBlockState(pos)
        if (blockState.fluidState.type === Fluids.WATER) {
            if (blockState.fluidState.isSource) {
                return entity.level().getBlockState(pos.above()).isAir
            }
        }
        return false
    }

    /**
     * Chooses between spawning a normal splash or glow splash depending on biome
     */
    private fun spawnSplash(world: Level, x: Double, y: Double, z: Double, data: SplashParticleInitialData?) {
        val splash = if (WaterUtils.isGlowingWater(world,
                BlockPos(x.toInt(), y.toInt(), z.toInt()))) ModParticles.GLOWING_SPLASH else ModParticles.SPLASH
        world.addParticle(splash.apply { initialData = data }, x, y, z, 0.0, 0.0, 0.0)
    }
}