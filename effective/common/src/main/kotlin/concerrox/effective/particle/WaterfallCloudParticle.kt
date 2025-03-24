package concerrox.effective.particle

import concerrox.effective.extension.isAir
import concerrox.effective.extension.isWater
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.*
import net.minecraft.core.particles.SimpleParticleType

class WaterfallCloudParticle(
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
        setParticleSpeed(dx, dy, dz)
        lifetime = 10
        quadSize = 1F
        setSpriteFromAge(spriteSet)
    }

    override fun tick() {
        super.tick()

        xo = x
        yo = y
        zo = z

        if (onGround || (age > 10 && level.isWater(x, y + yd, z))) {
            xd *= 0.5f
            yd *= 0.5f
            zd *= 0.5f
        }
        if (level.isWater(x, y + yd, z) && level.isAir(x, y, z)) {
            xd *= 0.9
            yd *= 0.9
            zd *= 0.9
        }

        xd *= 0.95f
        yd -= 0.02f
        zd *= 0.95f
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
            return WaterfallCloudParticle(level, x, y, z, velocityX, velocityY, velocityZ, spriteSet)
        }
    }

}