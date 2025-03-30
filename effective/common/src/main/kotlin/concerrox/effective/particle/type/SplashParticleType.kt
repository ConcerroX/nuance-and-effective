package concerrox.effective.particle.type

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.core.particles.SimpleParticleType
import kotlin.math.abs

@Environment(EnvType.CLIENT)
class SplashParticleType(alwaysShow: Boolean) : SimpleParticleType(alwaysShow) {
    var initialData: SplashParticleInitialData? = null

    class SplashParticleInitialData(val width: Float, velocityY: Double) {
        val velocityY: Double = abs(velocityY)
    }

}