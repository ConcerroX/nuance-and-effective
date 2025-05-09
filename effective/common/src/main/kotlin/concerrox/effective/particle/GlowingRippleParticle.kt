package concerrox.effective.particle

import concerrox.effective.util.WaterUtils
import concerrox.effective.util.greenFloat
import concerrox.effective.util.redFloat
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.client.renderer.LightTexture
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.SimpleParticleType

class GlowingRippleParticle(
    level: ClientLevel, x: Double, y: Double, z: Double, dx: Double, dy: Double, dz: Double, spriteSet: SpriteSet
) : RippleParticle(level, x, y, z, dx, dy, dz, spriteSet) {

    init {
        val color = WaterUtils.getGlowingWaterColor(level, BlockPos.containing(x, y, z))
        rCol = color.redFloat
        gCol = color.greenFloat
    }

    override fun getLightColor(partialTick: Float): Int {
        return LightTexture.FULL_BRIGHT
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
            return GlowingRippleParticle(level, x, y, z, velocityX, velocityY, velocityZ, spriteSet)
        }
    }
}