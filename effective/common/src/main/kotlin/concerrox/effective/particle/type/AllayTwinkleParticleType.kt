package concerrox.effective.particle.type

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.core.particles.SimpleParticleType
import java.awt.Color
import kotlin.math.abs

@Environment(EnvType.CLIENT)
class AllayTwinkleParticleType(alwaysShow: Boolean) : SimpleParticleType(alwaysShow) {
    var color: Color? = null
}