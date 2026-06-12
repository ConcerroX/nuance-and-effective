package concerrox.effective.particle

import com.mojang.blaze3d.vertex.VertexConsumer
import concerrox.effective.registry.ModParticles
import concerrox.effective.util.isAir
import concerrox.effective.util.isInBlockTag
import concerrox.effective.util.nextDoubleOrNegative
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Camera
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.ParticleRenderType
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.tags.BlockTags
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec3
import java.awt.Color
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.sin
import kotlin.math.sqrt


class WillOWispParticle(
    level: ClientLevel, x: Double, y: Double, z: Double,
//    private val texture: ResourceLocation,
    private val color: Color, private val targetColor: Color
) : Particle(level, x, y, z) {
    //    private val model = WillOWispModel(Minecraft.getInstance().entityModels.bakeLayer(WillOWispModel.MODEL_LAYER));
//    val layer = RenderType.entityTranslucent(texture)
    private var yaw = 0f
    private var pitch = 0f
    private var prevYaw = 0f
    private var prevPitch = 0f
    private var speedModifier = 0f

    private var xTarget = 0.0
    private var yTarget = 0.0
    private var zTarget = 0.0
    private var targetChangeCooldown = 0
    private var timeInSolid = -1

    init {
        gravity = 0F
        age = 0
        lifetime = 600 + random.nextInt(600)
        speedModifier = 0.1F + max(0F, random.nextFloat() - 0.1F)
//        rCol = red
//        gCol = green
//        bCol = blue
    }

    override fun getRenderType(): ParticleRenderType {
        return ParticleRenderType.CUSTOM
    }

    override fun render(buffer: VertexConsumer, renderInfo: Camera, partialTicks: Float) {
        fun addParticle(
            particleData: ParticleOptions,
            x: Double,
            y: Double,
            z: Double,
            xMotion: Double,
            yMotion: Double,
            zMotion: Double
        ) {
            level.addParticle(particleData, true, x, y, z, xMotion, yMotion, zMotion)
        }

        if (level.isInBlockTag(x, y, z, BlockTags.SOUL_FIRE_BASE_BLOCKS)) {
            level.addParticle(ParticleTypes.SOUL, x + random.nextDoubleOrNegative() / 10.0,
                y + random.nextDoubleOrNegative() / 10.0, z + random.nextDoubleOrNegative() / 10.0,
                random.nextDoubleOrNegative() / 20.0, random.nextDoubleOrNegative() / 20.0,
                random.nextDoubleOrNegative() / 20.0)
        } else {
            val x = Mth.lerp(partialTicks.toDouble(), xo, x)
            val y = Mth.lerp(partialTicks.toDouble(), yo, y)
            val z = Mth.lerp(partialTicks.toDouble(), zo, z)

            for (i in 0..1) {
                val particle = ModParticles.WISP.apply {
                    roll = random.nextGaussian().toFloat() / 5F
                    scale = /*if (this is PlayerWispParticle) ? 0.16f : */ 0.25F
                    color = this@WillOWispParticle.color
                    targetColor = this@WillOWispParticle.targetColor
                    lifetime = 40
                }
                addParticle(particle, x + random.nextGaussian() / 20.0, y + random.nextGaussian() / 20.0,
                    z + random.nextGaussian() / 20.0, 0.0, 0.066, 0.0)
            }

            val particle = ModParticles.WISP.apply {
                easingAlpha = true
                roll = random.nextGaussian().toFloat() / 5F
                scale = /*if (this is PlayerWispParticle) ? 0.10f : */ 0.15F
                color = Color.WHITE
                targetColor = Color.WHITE
                lifetime = 3
            }
            addParticle(particle, x, y, z, 0.0, 0.066, 0.0)
        }
    }

    override fun tick() {
        if (xo == x && yo == y && zo == z) {
            selectBlockTarget()
        }
        xo = x
        yo = y
        zo = z

        val blockPos = BlockPos.containing(x, y, z)
        if (age++ >= lifetime) {
            for (i in 0..49) {
//                for (j in 0..2) {
//                    val particle = ModParticles.WISP.apply {
//                        roll = random.nextFloatOrNegative() / 5F
//                        scale = 0.25F
//                        color = this@WillOWispParticle.color
//                        targetColor = this@WillOWispParticle.targetColor
//                        lifetime = 20
//                    }
//                    level.addParticle(particle, x, y, z, random.nextDoubleOrNegative() / 10.0, random.nextDoubleOrNegative() / 10.0, random.nextDoubleOrNegative() / 10.0)
//                }

//                level.addParticle(BlockParticleOption(ParticleTypes.BLOCK, Blocks.SOUL_SAND.defaultBlockState()),
//                    x + random.nextDoubleOrNegative() / 10, y + random.nextDoubleOrNegative() / 10,
//                    z + random.nextDoubleOrNegative() / 10, random.nextDoubleOrNegative() / 20,
//                    random.nextDoubleOrNegative() / 20, random.nextDoubleOrNegative() / 20)
            }

            level.playLocalSound(blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble(),
                SoundEvents.SOUL_ESCAPE, SoundSource.AMBIENT, 1F, 1.5F, true)
            level.playLocalSound(blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble(),
                SoundEvents.SOUL_SAND_BREAK, SoundSource.AMBIENT, 1F, 1F, true)
//            remove()
        }

        targetChangeCooldown -= if (Vec3(x, y, z).distanceToSqr(xo, yo, zo) < 0.0125) 10 else 1

        if (level.gameTime % 20 == 0L && ((xTarget == 0.0 && yTarget == 0.0 && zTarget == 0.0) || Vec3(x, y,
                z).distanceToSqr(xTarget, yTarget, zTarget) < 9 || targetChangeCooldown <= 0)) {
            selectBlockTarget()
        }

        var targetVector = Vec3(xTarget - x, yTarget - y, zTarget - z)
        targetVector = targetVector.scale(speedModifier / targetVector.length())

        xd = 0.9 * xd + 0.1 * targetVector.x
        yd = 0.9 * yd + 0.1 * targetVector.y
        zd = 0.9 * zd + 0.1 * targetVector.z
        prevYaw = yaw
        prevPitch = pitch

        val vec3d = Vec3(xd, yd, zd)
        yaw = (Mth.atan2(vec3d.x, vec3d.z) * 57.2957763671875).toFloat()
        pitch = (Mth.atan2(vec3d.y, sqrt(vec3d.x * vec3d.x + vec3d.z * vec3d.z)) * 57.2957763671875).toFloat()

        if (!BlockPos.containing(x, y, z).equals(getTargetPosition())) {
            move(xd, yd, zd)
        }

        if (random.nextInt(20) == 0) {
            level.playLocalSound(blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble(),
                SoundEvents.SOUL_ESCAPE, SoundSource.AMBIENT, 1F, 1.5F, true)
        }

        if (!level.isAir(x, y, z)) {
            if (timeInSolid > -1) {
                timeInSolid += 1
            }
        } else {
            timeInSolid = 0
        }
        if (timeInSolid > 25) {
            remove()
        }
    }

    override fun move(dx: Double, dy: Double, dz: Double) {
        var xd = dx
        var yd = dy
        var zd = dz
        val d = xd
        val e = yd
        if (hasPhysics && !level.isInBlockTag(x + xd, y + yd, z + zd,
                BlockTags.SOUL_FIRE_BASE_BLOCKS) && (xd != 0.0 || yd != 0.0 || zd != 0.0)) {
            val vec3d = Entity.collideBoundingBox(null, Vec3(xd, yd, zd), boundingBox, level, listOf())
            xd = vec3d.x
            yd = vec3d.y
            zd = vec3d.z
        }

        if (xd != 0.0 || yd != 0.0 || zd != 0.0) {
            boundingBox = boundingBox.move(xd, yd, zd)
            setLocationFromBoundingbox()
        }
        onGround = yd != yd && e < 0.0 && !level.isInBlockTag(x, y, z, BlockTags.SOUL_FIRE_BASE_BLOCKS)
        if (d != xd) {
            this.xd = 0.0
        }
        if (zd != zd) {
            this.zd = 0.0
        }
    }

    private fun getTargetPosition(): BlockPos {
        return BlockPos.containing(xTarget, yTarget + 0.5, zTarget)
    }

    private fun selectBlockTarget() {
        xTarget = x + random.nextDoubleOrNegative() * 10
        yTarget = y + random.nextDoubleOrNegative() * 10
        zTarget = z + random.nextDoubleOrNegative() * 10

        val targetPos = BlockPos.containing(xTarget, yTarget, zTarget)
        if (level.getBlockState(targetPos).isCollisionShapeFullBlock(level, targetPos) && !level.isInBlockTag(targetPos,
                BlockTags.SOUL_FIRE_BASE_BLOCKS)) {
            targetChangeCooldown = 0
            return
        }
        speedModifier = (0.1f + max(0.0, (random.nextFloat() - 0.1f).toDouble())).toFloat()
        targetChangeCooldown = random.nextInt() % (100 / speedModifier).toInt()
    }

    @Environment(EnvType.CLIENT)
    internal class Provider(
        private val spriteSet: SpriteSet, private val color: Color, private val targetColor: Color
    ) : ParticleProvider<SimpleParticleType> {
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
            return WillOWispParticle(level, x, y, z, color, targetColor)
        }
    }

}