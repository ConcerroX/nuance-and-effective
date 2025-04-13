package org.ladysnake.effective.particle

import concerrox.effective.effect.IlluminatedEffectsSpawner
import concerrox.effective.util.isNightTime
import concerrox.effective.util.nextDoubleOrNegative
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.ParticleRenderType
import net.minecraft.client.particle.SpriteSet
import net.minecraft.client.particle.TextureSheetParticle
import net.minecraft.client.renderer.LightTexture
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.world.level.Level
import net.minecraft.world.level.LightLayer
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.Vec3
import kotlin.math.max
import kotlin.math.min

class FireflyParticle(
    level: ClientLevel, x: Double, y: Double, z: Double, dx: Double, dy: Double, dz: Double, spriteSet: SpriteSet
) : TextureSheetParticle(level, x, y, z, dx, dy, dz) {

    private var nextAlphaGoal = 0F
    private var xTarget = 0.0
    private var yTarget = 0.0
    private var zTarget = 0.0
    private var maxHeight: Int
    private var lightTarget: BlockPos? = null

//    var light: PointLight

    init {
        lifetime = level.random.nextInt(400, 1200) // live between 20 seconds and one minute
        maxHeight = 4
        quadSize = 0.02F + random.nextFloat() * 0.02F
        alpha = 0F
        hasPhysics = false
        setParticleSpeed(dx, dy, dz)
        setSpriteFromAge(spriteSet)

        level.getBiome(BlockPos.containing(x, y, z)).unwrapKey().ifPresent {
            IlluminatedEffectsSpawner.fireflySpawnSettings[it]?.let { s ->
                rCol = s.color.red / 255F
                gCol = s.color.green / 255F
                bCol = s.color.blue / 255F
            }
        }
//        light = PointLight()
//        light.setBrightness(0f)
//        light.setColor(color.rgb)
//        light.setRadius(25f * scale)
//        light.setPosition(x, y, z)
//        if (EffectiveConfig.fireflyDynamicLights) {
//            EffectiveLights.PARTICLE_LIGHTS.add(light)
//            VeilRenderSystem.renderer().getLightRenderer().addLight(light)
//        }
    }

    override fun getLightColor(partialTick: Float): Int {
        return LightTexture.FULL_BRIGHT
    }

    override fun remove() {
        super.remove()
//        VeilRenderSystem.renderer().getLightRenderer().removeLight(light)
    }

    override fun tick() {
        xo = x
        yo = y
        zo = z
//        light.setPosition(x, y, z)
        // fade and die on daytime or if old enough unless fireflies can spawn any time of day
        if (!level.isNightTime || age++ >= lifetime) {
            nextAlphaGoal = 0F
            if (alpha <= 0.01F) {
                remove()
            }
        }

        // blinking
        if (alpha > nextAlphaGoal - BLINK_STEP && alpha < nextAlphaGoal + BLINK_STEP) {
            nextAlphaGoal = random.nextFloat()
        } else {
            if (nextAlphaGoal > alpha) {
                alpha = min(alpha + BLINK_STEP, 1F)
            } else if (nextAlphaGoal < alpha) {
                alpha = max(alpha - BLINK_STEP, 0F)
            }
        }
//        light.setBrightness(alpha * 4f)

        if (random.nextInt(20) == 0 || (xTarget == 0.0 && yTarget == 0.0 && zTarget == 0.0) || Vec3(x, y,
                z).distanceToSqr(xTarget, yTarget, zTarget) < 9) {
            selectBlockTarget()
        }

        var targetVector = Vec3(xTarget - x, yTarget - y, zTarget - z)
        val length = targetVector.length()
        targetVector = targetVector.scale(0.1 / length)

        val blockPos = BlockPos.containing(x, y - 0.1, z)
        if (!canFlyThroughBlock(level, blockPos, level.getBlockState(blockPos))) {
            xd = 0.9 * xd + 0.1 * targetVector.x
            yd = 0.05
            zd = 0.9 * zd + 0.1 * targetVector.z
        } else {
            xd = 0.9 * xd + 0.1 * targetVector.x
            yd = 0.9 * yd + 0.1 * targetVector.y
            zd = 0.9 * zd + 0.1 * targetVector.z
        }
        if (!BlockPos.containing(x, y, z).equals(BlockPos.containing(xTarget, yTarget + 0.5, zTarget))) {
            move(xd, yd, zd)
        }
    }

    override fun getRenderType(): ParticleRenderType {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT
    }

    private fun selectBlockTarget() {
        val immutableLightTarget = lightTarget
        if (immutableLightTarget == null) {
            var groundLevel = 0.0
            for (i in 0..19) {
                val checkedPos = BlockPos.containing(x, y - i, z)
                val checkedBlock = level.getBlockState(checkedPos)
                if (canFlyThroughBlock(level, checkedPos, checkedBlock)) {
                    groundLevel = y - i
                }
                if (groundLevel != 0.0) break
            }

            xTarget = x + random.nextDoubleOrNegative() * 10
            yTarget = min(max(y + random.nextDoubleOrNegative() * 2, groundLevel), groundLevel + maxHeight)
            zTarget = z + random.nextDoubleOrNegative() * 10

            val targetPos = BlockPos.containing(xTarget, yTarget, zTarget)
            if (!canFlyThroughBlock(level, targetPos, level.getBlockState(targetPos))) {
                yTarget += 1.0
            }
            lightTarget = mostLitBlockAround
        } else {
            xTarget = immutableLightTarget.x + random.nextDoubleOrNegative()
            yTarget = immutableLightTarget.y + random.nextDoubleOrNegative()
            zTarget = immutableLightTarget.z + random.nextDoubleOrNegative()
            lightTarget = if (level.getBrightness(LightLayer.BLOCK,
                    BlockPos.containing(x, y, z)) > 0 && level.isNight) {
                mostLitBlockAround
            } else {
                null
            }
        }
    }

    private val mostLitBlockAround: BlockPos
        get() {
            val randBlocks = HashMap<BlockPos, Int>()
            // get blocks adjacent to the fly
            for (x in -1..1) {
                for (y in -1..1) {
                    for (z in -1..1) {
                        val bp = BlockPos.containing((x + x).toDouble(), (y + y).toDouble(), (z + z).toDouble())
                        randBlocks[bp] = level.getBrightness(LightLayer.BLOCK, bp)
                    }
                }
            }
            // get other random blocks to find a different light source
            for (i in 0..14) {
                val randBP = BlockPos.containing(x + random.nextDoubleOrNegative() * 10,
                    y + random.nextDoubleOrNegative() * 10, z + random.nextDoubleOrNegative() * 10)
                randBlocks[randBP] = level.getBrightness(LightLayer.BLOCK, randBP)
            }
            return randBlocks.entries.stream().max { entry1, entry2 -> if (entry1.value > entry2.value) 1 else -1 }
                .get().key
        }

    @Environment(EnvType.CLIENT)
    internal class Provider(private val spriteSet: SpriteSet) : ParticleProvider<SimpleParticleType> {
        override fun createParticle(
            type: SimpleParticleType,
            level: ClientLevel,
            x: Double,
            y: Double,
            z: Double,
            velocityX: Double,
            velocityY: Double,
            velocityZ: Double
        ): Particle {
            return FireflyParticle(level, x, y, z, velocityX, velocityY, velocityZ, spriteSet)
        }
    }


    companion object {

        private const val BLINK_STEP = 0.05F

        fun canFlyThroughBlock(level: Level, blockPos: BlockPos, blockState: BlockState): Boolean {
            return !blockState.isSuffocating(level, blockPos) && blockState.fluidState.isEmpty
        }

    }
}