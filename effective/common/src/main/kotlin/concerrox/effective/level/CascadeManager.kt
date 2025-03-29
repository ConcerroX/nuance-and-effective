package concerrox.effective.level

import concerrox.effective.EffectiveConfig
import concerrox.effective.extension.isAir
import concerrox.effective.extension.isInBlockTag
import concerrox.effective.extension.isWaterFluid
import concerrox.effective.registry.ModParticles
import concerrox.effective.util.GlowUtils
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Vec3i
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.Level
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.level.material.Fluids
import java.awt.Color
import kotlin.math.sign
import kotlin.math.sqrt


object CascadeManager {

    private val generators = arrayListOf<Cascade>()
    private val scheduledGenerators = Object2IntOpenHashMap<Cascade>()

    fun reset() {
        generators.clear()
        scheduledGenerators.clear()
    }

    fun tick() {
        val minecraft = Minecraft.getInstance()
        val level = minecraft.level
        val player = minecraft.player
        if (minecraft.isPaused || level == null || player == null) {
            return
        }
        synchronized(generators) {
            tickParticles(level)
            if (level.gameTime % 3L != 0L) {
                return
            }
            generators.forEach {
                scheduleParticleTick(it, 6)
                val distance = sqrt(player.blockPosition().distSqr(it.blockPos))
//                if (cascade.isSilent || distance > EffectiveConfig.cascadeSoundDistanceBlocks || EffectiveConfig.cascadeSoundsVolume === 0 || EffectiveConfig.cascadeSoundDistanceBlocks === 0) {
//                    return@forEach
//                }
//                if (EffectiveUtils.isInCave(world, cascade.blockPos()) === EffectiveUtils.isInCave(world,
//                        client.player.getBlockPos()) && world.random.nextInt(
//                        200) === 0) { // make it so cascades underground can only be heard by players underground, and surface cascades can only be heard by players on the surface
//                    client.getSoundManager().play(cascadeSoundInstance.ambient(EffectiveSounds.AMBIENT_cascade,
//                        1.2f + world.random.nextFloat() / 10f, cascade.blockPos(),
//                        EffectiveConfig.cascadeSoundDistanceBlocks), (distance / 2).toInt())
//                }
            }
            generators.removeIf {
                createCascade(level, it.blockPos, level.getFluidState(it.blockPos)) == null
            }
        }
    }

    private fun tickParticles(level: ClientLevel) {
        scheduledGenerators.keys.forEach {
            if (it != null) {
                scheduledGenerators.computeInt(it) { _, integer -> integer - 1 }
                spawnCascadeCloud(level, it)
            }
        }
        scheduledGenerators.values.removeIf { integer -> integer < 0 }
    }

    fun scheduleParticleTick(cascade: Cascade, ticks: Int) {
        scheduledGenerators.put(cascade, ticks)
    }

    fun tryToAddCascadeGenerator(fluidState: FluidState, pos: BlockPos) {
        val level = Minecraft.getInstance().level
        if (!EffectiveConfig.isCascadeEnabled.get() || fluidState.type !== Fluids.FLOWING_WATER || level == null) {
            return
        }
        val cascadeOrNull = createCascade(level, pos, fluidState)
        synchronized(generators) {
            generators.removeIf { it.blockPos == pos }
            cascadeOrNull?.let { generators.add(it) }
        }
    }

    private fun createCascade(level: ClientLevel, pos: BlockPos, fluidState: FluidState): Cascade? {
        val minecraft = Minecraft.getInstance()
        val player = minecraft.player
        val isSilent: Boolean
        var mistColor = Color(0xFFFFFF)

        if (!EffectiveConfig.isCascadeEnabled.get() || fluidState.type != Fluids.FLOWING_WATER || player == null) {
            return null
        }
        val isInRenderDistance = sqrt(pos.distSqr(player.blockPosition())) <= minecraft.options.renderDistance()
            .get() * 32
        if (!isInRenderDistance) {
            return null
        }

        val mutableBlockPos = BlockPos.MutableBlockPos()
        fun setPos(vec3i: Vec3i, x: Int, y: Int, z: Int) = mutableBlockPos.setWithOffset(vec3i, x, y, z)

        if (level.isWaterFluid(setPos(pos, 0, -1, 0))) {
            if (level.isWaterFluid(setPos(pos, 0, -2, 0))) {
                isSilent = false
            } else if (level.isInBlockTag(setPos(pos, 0, -2, 0), BlockTags.WOOL)) {
                isSilent = true
                mistColor = Color(level.getBlockState(setPos(pos, 0, -2, 0)).getMapColor(level, pos).col)
            } else {
                return null
            }
        } else {
            return null
        }

        var foundAir = false
        for (x in -1..1 step 2) {
            for (z in -1..1 step 2) {
                if (level.isAir(mutableBlockPos.set(pos.x + x, pos.y, pos.z + z))) {
                    foundAir = true
                    break
                }
            }
        }
        if (!foundAir) {
            return null
        }

        var waterLandingSize = 0F
        arrayOf(Direction.DOWN, Direction.UP).forEach {
            if (level.isWaterFluid(mutableBlockPos.set(pos.x + it.stepX, pos.y - 1, pos.z + it.stepZ))) {
                waterLandingSize += 1F
            }
        }
        if (waterLandingSize >= 1f) {
            return Cascade(pos, fluidState.ownHeight + (waterLandingSize - 2F) / 2F, isSilent, mistColor)
        }
        return null
    }

    fun spawnCascadeCloud(level: Level, cascade: Cascade) {
        val pos = cascade.blockPos
        for (i in 0..EffectiveConfig.cascadeCloudDensity.get()) {
            val rand = level.random
            val offsetX = 0.5 + rand.nextGaussian() / 5.0
            val offsetY = rand.nextDouble()
            val offsetZ = 0.5 + rand.nextGaussian() / 5.0
            val particle = if (GlowUtils.isGlowingWater(level, cascade.blockPos)) {
                ModParticles.GLOWING_CASCADE_CLOUD
            } else {
                ModParticles.CASCADE_CLOUD
            }
            level.addParticle(particle, pos.x + offsetX, pos.y + offsetY, pos.z + offsetZ,
                sign(offsetX) * rand.nextFloat() * cascade.strength / 10.0, rand.nextFloat() * cascade.strength / 10.0,
                sign(offsetZ) * rand.nextFloat() * cascade.strength / 10.0)
        }
//        if (EffectiveConfig.cascadeMistDensity > 0f && cascade.strength() >= 1.6f) {
//            if ((world.random.nextFloat() * 100f) <= EffectiveConfig.cascadeMistDensity) {
//                double offsetX = world.getRandom().nextGaussian() / 5f;
//                double offsetZ = world.getRandom().nextGaussian() / 5f;
//                world.addParticle(EffectiveParticles.MIST, blockPos.getX() + .5f, blockPos.getY() + .5f, blockPos.getZ() + .5f, EffectiveUtils.getRandomFloatOrNegative(world.random) / 15f, EffectiveUtils.getRandomFloatOrNegative(world.random) / 30f, EffectiveUtils.getRandomFloatOrNegative(world.random) / 15f);
//            }
//        }

    }

    data class Cascade(val blockPos: BlockPos, val strength: Float, val isSilent: Boolean, val mistColor: Color)

}