package concerrox.effective.particle

import com.mojang.logging.LogUtils
import concerrox.effective.particle.type.WispParticleType
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
import net.minecraft.core.particles.SimpleParticleType


class WispParticle(
    level: ClientLevel,
    x: Double,
    y: Double,
    z: Double,
    dx: Double,
    dy: Double,
    dz: Double,
    spriteSet: SpriteSet,
    private val data: WispParticleType
) : TextureSheetParticle(level, x, y, z, dx, dy, dz) {

    init {
        alpha = if (data.easingAlpha) 0.2F else 1F
        roll = data.roll
        quadSize = data.scale
        hasPhysics = false
        data.color?.let {
            rCol = it.redFloat
            gCol = it.greenFloat
            bCol = it.blueFloat
        }
//        setParticleSpeed(dx, dy, dz)
        setSpriteFromAge(spriteSet)
    }

    override fun getRenderType(): ParticleRenderType {
        return ModParticleRenderTypes.ADDITIVE
    }

    override fun tick() {
        super.tick()
//        xo = x
//        yo = y
//        zo = z
//        if (age++ >= lifetime) {
//            remove()
//        } else {
//            move(xd, yd, zd)
//        }
        val age = age.toFloat()
        val lifetime = lifetime.toFloat()
        val color = data.color ?: return
        val targetColor = data.targetColor ?: return
        rCol = Easing.CIRC_OUT.ease(age, color.redFloat, targetColor.redFloat, lifetime)
        gCol = Easing.CIRC_OUT.ease(age, color.greenFloat, targetColor.greenFloat, lifetime)
        bCol = Easing.CIRC_OUT.ease(age, color.blueFloat, targetColor.blueFloat, lifetime)
        quadSize = Easing.CIRC_OUT.ease(age, data.scale, 0F, lifetime)
        alpha = Easing.LINEAR.ease(age, if (data.easingAlpha) 0.2F else 1F, 0F, lifetime)
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
            return WispParticle(level, x, y, z, velocityX, velocityY, velocityZ, spriteSet, type as WispParticleType)
        }
    }

}