package concerrox.effective.particle

import com.mojang.blaze3d.vertex.VertexConsumer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Camera
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.ParticleRenderType
import net.minecraft.client.particle.SpriteSet
import net.minecraft.client.particle.TextureSheetParticle
import net.minecraft.core.particles.SimpleParticleType
import org.joml.Quaternionf

open class RippleParticle(
    level: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    dx: Double,
    dy: Double,
    dz: Double,
    private val spriteSet: SpriteSet
) : TextureSheetParticle(level, x, y, z, dx, dy, dz) {

    init {
        val scaleAgeModifier = 1 + random.nextInt(10)
        quadSize *= 2f + random.nextFloat() / 10f * scaleAgeModifier
        lifetime = 10 + random.nextInt(scaleAgeModifier)
        this.setParticleSpeed(0.0, 0.0, 0.0)
        this.setSpriteFromAge(spriteSet)
    }

    override fun getRenderType(): ParticleRenderType {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT
    }

    override fun tick() {
        xo = x
        yo = y
        zo = z
        if (age++ >= lifetime) {
            remove()
        }
        setSpriteFromAge(spriteSet)
    }

    override fun render(vertexConsumer: VertexConsumer, camera: Camera, tickDelta: Float) {
        val quaternion = Quaternionf()
        quaternion.rotateX(Math.toRadians(-90.0).toFloat())
        renderRotatedQuad(vertexConsumer, camera, quaternion, tickDelta)
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
            return RippleParticle(level, x, y, z, velocityX, velocityY, velocityZ, spriteSet)
        }
    }
}