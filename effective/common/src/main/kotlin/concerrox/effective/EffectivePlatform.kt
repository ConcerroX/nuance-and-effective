package concerrox.effective

import dev.architectury.injectables.annotations.ExpectPlatform
import net.minecraft.core.particles.ParticleType
import net.minecraft.resources.ResourceLocation

@ExpectPlatform
fun registerParticle(id: ResourceLocation, type: ParticleType<*>) {
    throw NotImplementedError()
}