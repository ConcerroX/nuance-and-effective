package concerrox.effective.particle

import concerrox.effective.registry.ModParticles
import concerrox.effective.util.isAir
import concerrox.effective.util.isWater
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
import kotlin.math.min
import kotlin.math.roundToInt

class GlowingDropletParticle(
    level: ClientLevel, x: Double, y: Double, z: Double, dx: Double, dy: Double, dz: Double, spriteSet: SpriteSet
) : DropletParticle(level, x, y, z, dx, dy, dz, spriteSet) {

    init {
        val redAndGreen = min(1F, level.getBrightness(LightLayer.BLOCK, BlockPos.containing(x, y, z)) / 15F)
        rCol = redAndGreen
        gCol = redAndGreen
    }

    override fun getLightColor(partialTick: Float): Int {
        return LightTexture.FULL_BRIGHT
    }

    override fun tick() {
        xo = x
        yo = y
        zo = z

        if (age++ >= lifetime) {
            remove()
        }

        if (onGround || (age > 5 && level.isWater(x, y + xd, z))) {
            remove()
        }

        if (level.isWater(x, y + xd, z) && level.isAir(x, y, z)) {
            for (i in 0 downTo -10 + 1) {
                val pos = BlockPos.containing(x, (y.roundToInt() + i).toDouble(), z)
                val posAbove = BlockPos.containing(x, (y.roundToInt() + i + 1).toDouble(), z)
                if (level.isWater(pos) && level.getFluidState(pos).isSource && level.isAir(posAbove)) {
                    level.addParticle(ModParticles.GLOWING_RIPPLE, x, Math.round(y) + i + 0.9, z, 0.0, 0.0, 0.0)
                    break
                }
            }

            remove()
        }

        xd *= 0.99f
        yd -= 0.05f
        zd *= 0.99f

        move(xd, yd, zd)
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
            return GlowingDropletParticle(level, x, y, z, velocityX, velocityY, velocityZ, spriteSet)
        }
    }
}