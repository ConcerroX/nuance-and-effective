package concerrox.effective.level

import concerrox.effective.EffectiveConfig
import concerrox.effective.extension.isAir
import concerrox.effective.extension.isInBlockTag
import concerrox.effective.extension.isWaterFluid
import concerrox.effective.registry.ModParticles
import concerrox.effective.registry.ModSounds
import concerrox.effective.sound.CascadeSoundInstance
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
                if (it.isSilent || distance > EffectiveConfig.cascadeSoundDistanceBlocks.get() || EffectiveConfig.cascadeSoundsVolume.get() == 0 || EffectiveConfig.cascadeSoundDistanceBlocks.get() == 0) {
                    return@forEach
                }
                if (isInCave(level, it.blockPos) == isInCave(level, player.blockPosition()) && level.random.nextInt(
                        200) == 0) {
                    // make it so cascades underground can only be heard by players underground, and surface cascades can only be heard by players on the surface
                    val sound = CascadeSoundInstance.ambient(soundEvent = ModSounds.AMBIENT_CASCADE,
                        pitch = 1.2F + level.random.nextFloat() / 10F, blockPos = it.blockPos,
                        maxDistance = EffectiveConfig.cascadeSoundDistanceBlocks.get().toFloat())
                    val delay = (distance / 2).toInt()
                    minecraft.soundManager.playDelayed(sound, delay)
                }
            }
            generators.removeIf {
                createCascade(level, it.blockPos, level.getFluidState(it.blockPos)) == null
            }
        }
    }

    // method to check if the player has a stone material type block above them, more reliable to
    // detect caves compared to isSkyVisible (okay nvm they removed materials we're using pickaxe mine-able instead lmao oh god this is
    // going to be so unreliable)
    private fun hasStoneAbove(level: Level, pos: BlockPos): Boolean {
        val mutable = pos.mutable()
        val startY = mutable.y
        for (y in startY..startY + 100) {
            mutable.setY(y)
            val state = level.getBlockState(mutable)
            if (state.isRedstoneConductor(level, pos) && state.`is`(BlockTags.MINEABLE_WITH_PICKAXE)) {
                return true
            }
        }
        return false
    }

    private fun isInCave(level: Level, pos: BlockPos): Boolean = pos.y < level.seaLevel && hasStoneAbove(level, pos)

    private fun tickParticles(level: ClientLevel) {
        scheduledGenerators.keys.forEach {
            scheduledGenerators.computeInt(it) { _, integer -> integer - 1 }
            spawnCascadeCloud(level, it)
        }
        scheduledGenerators.values.removeIf { integer -> integer < 0 }
    }

    fun scheduleParticleTick(cascade: Cascade, ticks: Int) {
        scheduledGenerators.put(cascade, ticks)
    }

    fun tryToAddCascadeGenerator(fluidState: FluidState, pos: BlockPos) {
        val level = Minecraft.getInstance().level
        if (!EffectiveConfig.isCascadesEnabled.get() || fluidState.type !== Fluids.FLOWING_WATER || level == null) {
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

        if (!EffectiveConfig.isCascadesEnabled.get() || fluidState.type != Fluids.FLOWING_WATER || player == null) {
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
        Direction.values().filter { it.axis != Direction.Axis.Y }.forEach {
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
        val random = level.random
        for (i in 0..EffectiveConfig.cascadeCloudDensity.get()) {
            val offsetX = random.nextGaussian() / 5.0
            val offsetY = random.nextDouble()
            val offsetZ = random.nextGaussian() / 5.0
            val particle = if (GlowUtils.isGlowingWater(level, cascade.blockPos)) {
                ModParticles.GLOWING_CASCADE_CLOUD
            } else {
                ModParticles.CASCADE_CLOUD
            }
            level.addParticle(
                particle,
                pos.x + 0.5 + offsetX,
                pos.y + offsetY,
                pos.z + 0.5 + offsetZ,
                sign(offsetX) * random.nextFloat() * cascade.strength / 10.20,
                random.nextFloat() * cascade.strength / 10.0,
                sign(offsetZ) * random.nextFloat() * cascade.strength / 10.0,
            )
        }
        if (EffectiveConfig.cascadeMistDensity.get() > 0.0 && cascade.strength >= 1.6F) {
            if ((random.nextFloat() * 100F) <= EffectiveConfig.cascadeMistDensity.get()) {
                // Disabled since the rendering is buggy without soft render type
//                level.addParticle(ModParticles.MIST, pos.x + 0.5, pos.y + 0.5, pos.z + 0.5,
//                    random.nextDoubleOrNegative() / 15.0, random.nextDoubleOrNegative() / 30.0,
//                    random.nextDoubleOrNegative() / 15.0)
            }
        }
    }

    data class Cascade(
        val blockPos: BlockPos,
        val strength: Float,
        val isSilent: Boolean,
        val mistColor: Color,
    )
}
