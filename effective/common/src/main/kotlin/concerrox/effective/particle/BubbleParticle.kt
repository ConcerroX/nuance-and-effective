package concerrox.effective.particle

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


open class BubbleParticle(
    level: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    dx: Double,
    dy: Double,
    dz: Double,
    spriteSet: SpriteSet
) : TextureSheetParticle(level, x, y, z, dx, dy, dz) {

    init {
        friction = 0.9F
        lifetime = 60 + level.random.nextInt(60)
        quadSize = 0.05F + level.random.nextFloat() * 0.05F
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
        
        if (age++ >= lifetime || !level.isWaterAt(BlockPos.containing(x, y, z))) {
            remove()
        } else {
            move(xd, yd, zd)
            if (speedUpWhenYMotionIsBlocked && y == yo) {
                xd *= 1.1
                zd *= 1.1
            }

            xd *= friction.toDouble()
            zd *= friction.toDouble()
            if (onGround) {
                xd *= 0.7f
                zd *= 0.7f
            }
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
            return BubbleParticle(level, x, y, z, velocityX, velocityY, velocityZ, spriteSet)
        }
    }

}