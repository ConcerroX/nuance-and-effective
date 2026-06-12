package concerrox.effective.particle.type

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.core.particles.SimpleParticleType
import java.awt.Color
import kotlin.math.abs

@Environment(EnvType.CLIENT)
class WispParticleType(alwaysShow: Boolean) : SimpleParticleType(alwaysShow) {
    var roll = 0F
    var scale = 0F
    var color: Color? = null
    var targetColor: Color? = null
    var easingAlpha = false
}