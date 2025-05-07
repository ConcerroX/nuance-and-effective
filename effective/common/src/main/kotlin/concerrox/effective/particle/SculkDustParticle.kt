package concerrox.effective.particle

import concerrox.effective.render.ModParticleRenderTypes
import concerrox.effective.screenshake.Easing
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
import net.minecraft.client.renderer.LightTexture
import net.minecraft.core.particles.SimpleParticleType
import java.awt.Color


class SculkDustParticle(
    level: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    dx: Double,
    dy: Double,
    dz: Double,
    spriteSet: SpriteSet,
) : TextureSheetParticle(level, x, y, z, dx, dy, dz) {

    private val bright = random.nextInt(50) == 0

    init {
        quadSize = 0.02F
        lifetime = 100 + random.nextInt(50) / 2 * 2
        val color = if (bright) Color(0x29DFEB) else Color(0x0D1217)
        color.let {
            rCol = it.redFloat
            gCol = it.greenFloat
            bCol = it.blueFloat
        }
        setParticleSpeed(dx, dy, dz)
        setSpriteFromAge(spriteSet)
    }

    override fun getRenderType(): ParticleRenderType {
        return if (bright) ModParticleRenderTypes.ADDITIVE else ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT
    }

    override fun getLightColor(partialTick: Float): Int {
        return if (bright) LightTexture.FULL_BRIGHT else -1
    }

    override fun tick() {
        alpha = if (age > lifetime / 2) {
            Easing.SINE_OUT.ease(age.toFloat(), 1F, 1F, (lifetime / 2).toFloat())
        } else {
            Easing.SINE_OUT.ease((age - lifetime / 2).toFloat(), 1F, 0F, (lifetime / 2).toFloat())
        }
        super.tick()
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
            return SculkDustParticle(level, x, y, z, velocityX, velocityY, velocityZ, spriteSet)
        }
    }

}