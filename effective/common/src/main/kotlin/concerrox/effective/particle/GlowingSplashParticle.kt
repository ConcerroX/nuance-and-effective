package concerrox.effective.particle

import concerrox.effective.particle.type.SplashParticleType
import concerrox.effective.registry.ModParticles
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.client.renderer.LightTexture
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.world.level.LightLayer
import java.awt.Color
import kotlin.math.min

class GlowingSplashParticle(level: ClientLevel, x: Double, y: Double, z: Double) : SplashParticle(level, x, y, z) {

    override fun getRimBrightness(tickDelta: Float): Int {
        return LightTexture.FULL_BRIGHT
    }

    override fun getRimColor(pos: BlockPos): Color {
        val redAndGreen = min(1F, level.getBrightness(LightLayer.BLOCK, pos) / 15F)
        return Color(redAndGreen, redAndGreen, bCol, 1F)
    }

    override fun getDropletParticle(): SimpleParticleType {
        return ModParticles.GLOWING_DROPLET
    }

    @Environment(EnvType.CLIENT)
    internal class Provider(spriteSet: SpriteSet) : ParticleProvider<SimpleParticleType> {
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
            val instance = GlowingSplashParticle(level, x, y, z)
            (type as SplashParticleType).let {
                val width = it.width * 2
                instance.widthMultiplier = width
                instance.heightMultiplier = it.velocityY.toFloat() * width
                instance.wave1End = 10 + Math.round(width * 1.2f)
                instance.wave2Start = 6 + Math.round(width * 0.7f)
                instance.wave2End = 20 + Math.round(width * 2.4f)
            }
            return instance
        }
    }
}