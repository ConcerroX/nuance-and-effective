package concerrox.effective.particle

import concerrox.effective.extension.isAir
import concerrox.effective.extension.isWater
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.*
import net.minecraft.core.particles.SimpleParticleType

open class CascadeCloudParticle(
    level: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    dx: Double,
    dy: Double,
    dz: Double,
    private val spriteSet: SpriteSet,
) : TextureSheetParticle(level, x, y, z, dx, dy, dz) {

    init {
        lifetime = 10
        quadSize = 1F
        alpha = 0.9F
        setParticleSpeed(dx, dy, dz)
        setSpriteFromAge(spriteSet)
    }

    override fun tick() {
        super.tick()

        xo = x
        yo = y
        zo = z

        if (onGround || level.isWater(x, y + yd, z)) {
            xd *= 0.5
            yd *= 0.5
            zd *= 0.5
        }
        if (level.isWater(x, y + yd, z) && level.isAir(x, y, z)) {
            xd *= 0.9
            yd *= 0.9
            zd *= 0.9
        }

        xd *= 0.95
        yd -= 0.02
        zd *= 0.95
        move(xd, yd, zd)

        alpha -= 0.05F
        setSpriteFromAge(spriteSet)
    }

    override fun getRenderType(): ParticleRenderType {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT
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
            return CascadeCloudParticle(level, x, y, z, velocityX, velocityY, velocityZ, spriteSet)
        }
    }

}