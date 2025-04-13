package concerrox.effective.particle

import com.mojang.blaze3d.vertex.VertexConsumer
import concerrox.effective.registry.ModParticles
import concerrox.effective.util.getBlockState
import concerrox.effective.util.isAir
import concerrox.effective.util.isWater
import concerrox.effective.util.nextDoubleOrNegative
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Camera
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.ParticleRenderType
import net.minecraft.client.particle.SpriteSet
import net.minecraft.client.particle.TextureSheetParticle
import net.minecraft.client.renderer.LightTexture
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.tags.FluidTags
import net.minecraft.util.Mth
import org.joml.Quaternionf
import org.joml.Vector3f
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class ChorusPetalParticle(
    level: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    dx: Double,
    dy: Double,
    dz: Double,
    spriteSet: SpriteSet
) : TextureSheetParticle(level, x, y, z, dx, dy, dz) {

    private var rotationFactor = random.nextDoubleOrNegative() * 0.1
    private val groundOffset = random.nextFloat() / 100f + 0.01f
    private var isInAir = true

    init {
        lifetime = 300 + random.nextInt(60)
        quadSize *= 1F + random.nextFloat()
        hasPhysics = true
        setSprite(spriteSet.get(random.nextInt(3), 2))
        setParticleSpeed(dx, dy, dz)

        if (yd == 0.0 && xd == 0.0 && zd == 0.0) {
            alpha = 0F
        }
        yd = yd - 0.15 - random.nextDouble() / 10
        xd = xd - 0.05 - random.nextDouble() / 10
        zd = zd - 0.05 - random.nextDouble() / 10
        roll = random.nextFloat() * 360F
    }

    override fun render(vertexConsumer: VertexConsumer, camera: Camera, tickDelta: Float) {
        if (isInAir) {
            super.render(vertexConsumer, camera, tickDelta)
        } else {
            val quaternion = Quaternionf()
            quaternion.rotationYXZ(0F, Math.toRadians(-90.0).toFloat(), roll)
            renderRotatedQuad(vertexConsumer, camera, quaternion, tickDelta)
        }
    }

    private fun renderRotatedQuad(
        vertexConsumer: VertexConsumer, camera: Camera, quaternion: Quaternionf, tickDelta: Float
    ) {
        val vec3d = camera.position
        val g = (Mth.lerp(tickDelta.toDouble(), xo, x) - vec3d.x).toFloat()
        val h = (Mth.lerp(tickDelta.toDouble(), yo, y) - vec3d.y).toFloat() + groundOffset
        val i = (Mth.lerp(tickDelta.toDouble(), zo, z) - vec3d.z).toFloat()
        renderRotatedQuad(vertexConsumer, quaternion, g, h, i, tickDelta)
    }

    private fun renderRotatedQuad(
        vertexConsumer: VertexConsumer, quaternion: Quaternionf, g: Float, h: Float, i: Float, tickDelta: Float
    ) {
        val vec3f = Vector3f(-1.0f, -1.0f, 0.0f)
        vec3f.rotate(quaternion)
        val vector3fs = arrayOf(Vector3f(-1.0f, -1.0f, 0.0f), Vector3f(-1.0f, 1.0f, 0.0f), Vector3f(1.0f, 1.0f, 0.0f),
            Vector3f(1.0f, -1.0f, 0.0f))

        for (k in 0..3) {
            val vector3f = vector3fs[k]
            vector3f.rotate(Quaternionf().rotateXYZ(Math.toRadians(90.0).toFloat(), 0f, 0f))
            vector3f.mul(getQuadSize(tickDelta))
            vector3f.add(g, h, i)
        }

        val minU = u0
        val maxU = u1
        val minV = v0
        val maxV = v1
        val lightColor = getLightColor(tickDelta)

        vertexConsumer.vertex(vector3fs[0].x().toDouble(), vector3fs[0].y().toDouble(), vector3fs[0].z().toDouble())
            .uv(maxU, maxV).color(rCol, gCol, bCol, alpha).uv2(lightColor).endVertex()
        vertexConsumer.vertex(vector3fs[1].x().toDouble(), vector3fs[1].y().toDouble(), vector3fs[1].z().toDouble())
            .uv(maxU, minV).color(rCol, gCol, bCol, alpha).uv2(lightColor).endVertex()
        vertexConsumer.vertex(vector3fs[2].x().toDouble(), vector3fs[2].y().toDouble(), vector3fs[2].z().toDouble())
            .uv(minU, minV).color(rCol, gCol, bCol, alpha).uv2(lightColor).endVertex()
        vertexConsumer.vertex(vector3fs[3].x().toDouble(), vector3fs[3].y().toDouble(), vector3fs[3].z().toDouble())
            .uv(minU, maxV).color(rCol, gCol, bCol, alpha).uv2(lightColor).endVertex()
    }

    override fun getLightColor(partialTick: Float): Int {
        return LightTexture.FULL_BRIGHT
    }

    override fun getRenderType(): ParticleRenderType {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT
    }

    override fun tick() {
        xo = x
        yo = y
        zo = z

        if (age++ < lifetime) {
            alpha = min(1F, alpha + 0.1F)
        }

        move(xd, yd, zd)
        xd *= 0.99
        yd *= 0.99
        zd *= 0.99

        gCol /= 1.002F
        rCol = max(gCol / 1.001F, 0.3F)

        if (age >= lifetime) {
            alpha = max(0F, alpha - 0.1F)
            if (alpha <= 0f) {
                remove()
            }
        }

        oRoll = roll
        if ((onGround || level.getFluidState(BlockPos.containing(x, y, z)).`is`(FluidTags.WATER)) && isInAir) {
            if (level.isWater(x, y, z)) {
                for (i in 0 downTo -10 + 1) {
                    val yOffset = (y.roundToInt() + i).toDouble()
                    val pos = BlockPos.containing(x, yOffset, z)
                    if (level.isWater(pos) && level.getBlockState(x, yOffset, z).fluidState.isSource && level.isAir(x,
                            yOffset + 1, z)) {
                        level.addParticle(ModParticles.RIPPLE, x, yOffset + 0.9f, z, 0.0, 0.0, 0.0)
                        break
                    }
                }
            }
            xd = 0.0
            yd = 0.0
            zd = 0.0
            isInAir = false
        }

        if (yd != 0.0) {
            roll = (Math.PI * rotationFactor * age).toFloat()
        }
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
            return ChorusPetalParticle(level, x, y, z, velocityX, velocityY, velocityZ, spriteSet)
        }
    }

}