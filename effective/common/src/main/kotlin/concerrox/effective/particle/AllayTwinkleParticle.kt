package concerrox.effective.particle

import concerrox.effective.particle.type.AllayTwinkleParticleType
import concerrox.effective.util.blueFloat
import concerrox.effective.util.greenFloat
import concerrox.effective.util.redFloat
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.ParticleRenderType
import net.minecraft.client.particle.SpriteSet
import net.minecraft.client.particle.TextureSheetParticle
import net.minecraft.core.particles.SimpleParticleType


class AllayTwinkleParticle(
    level: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    dx: Double,
    dy: Double,
    dz: Double,
    private val spriteSet: SpriteSet,
    data: AllayTwinkleParticleType
) : TextureSheetParticle(level, x, y, z, dx, dy, dz) {

    init {
        alpha = 0.9F
        quadSize = 0.12F
        data.color?.let {
            rCol = it.redFloat
            gCol = it.greenFloat
            bCol = it.blueFloat
        }
        lifetime = 15
        setParticleSpeed(dx, dy, dz)
        setSpriteFromAge(spriteSet)
    }

    override fun getRenderType(): ParticleRenderType {
        return ParticleRenderType.PARTICLE_SHEET_LIT
    }

    override fun tick() {
        super.tick()
        if (age < lifetime) {
            setSprite(spriteSet.get(age / 3, 6))
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
            return AllayTwinkleParticle(level, x, y, z, velocityX, velocityY, velocityZ, spriteSet, type as AllayTwinkleParticleType)
        }
    }

}