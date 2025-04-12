package concerrox.effective.particle

import concerrox.effective.registry.ModParticles
import concerrox.effective.util.isAir
import concerrox.effective.util.isWater
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.ParticleRenderType
import net.minecraft.client.particle.SpriteSet
import net.minecraft.client.particle.TextureSheetParticle
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.SimpleParticleType
import kotlin.math.roundToInt


open class DropletParticle(
    level: ClientLevel, x: Double, y: Double, z: Double, dx: Double, dy: Double, dz: Double, spriteSet: SpriteSet
) : TextureSheetParticle(level, x, y, z, dx, dy, dz) {

    init {
        lifetime = 500
        quadSize = 0.05F
        this.setParticleSpeed(dx, dy, dz)
        this.setSpriteFromAge(spriteSet)
    }

    override fun getRenderType(): ParticleRenderType {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE
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
                    level.addParticle(ModParticles.RIPPLE, x, Math.round(y) + i + 0.9, z, 0.0, 0.0, 0.0)
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
            return DropletParticle(level, x, y, z, velocityX, velocityY, velocityZ, spriteSet)
        }
    }
}