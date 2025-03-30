package concerrox.effective.particle

import concerrox.effective.render.ModParticleRenderTypes
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.Particle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.ParticleRenderType
import net.minecraft.client.particle.SpriteSet
import net.minecraft.client.particle.TextureSheetParticle
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.util.Mth

class MistParticle(
    level: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    dx: Double,
    dy: Double,
    dz: Double,
    spriteSet: SpriteSet,
) : TextureSheetParticle(level, x, y, z, dx, dy, dz) {

    init {
        lifetime = 300
        quadSize = 10F + level.random.nextFloat() * 5F
        alpha = 0.001F
        friction = 0.999F
        this.setParticleSpeed(dx, dy, dz)
        this.setSpriteFromAge(spriteSet)
    }

    override fun getRenderType(): ParticleRenderType = if (Minecraft.useShaderTransparency()) {
        ParticleRenderType.NO_RENDER
    } else {
        ParticleRenderType.NO_RENDER
    }

    override fun tick() {
        super.tick()
        alpha = if (age <= 20) {
            Mth.lerp(age / 20F, 0.0F, 0.2F)
        } else {
            Mth.lerp((age - 20F) / lifetime, 0.2F, 0.0F)
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
            return MistParticle(level, x, y, z, velocityX, velocityY, velocityZ, spriteSet)
        }
    }

}