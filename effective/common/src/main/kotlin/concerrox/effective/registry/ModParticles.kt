package concerrox.effective.registry

import concerrox.effective.Effective
import concerrox.effective.particle.WaterfallCloudParticle
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.resources.ResourceLocation

object ModParticles {

    val PARTICLES = mutableListOf<ParticleEntry<*>>()

    private val WATERFALL_CLOUD by register("waterfall_cloud", WaterfallCloudParticle::Provider) { SimpleParticleType(true) }

    private fun <T : ParticleOptions> register(
        id: String, provider: (SpriteSet) -> ParticleProvider<T>, type: () -> ParticleType<T>
    ): Lazy<ParticleType<T>> {
        val lazyType = lazy { type.invoke() }
        val entry = ParticleEntry(Effective.id(id), lazyType, provider)
        PARTICLES += entry
        return lazyType
    }

    fun initialize() {
        WATERFALL_CLOUD
    }

    data class ParticleEntry<T : ParticleOptions>(
        val id: ResourceLocation,
        val type: Lazy<ParticleType<T>>,
        val provider: (SpriteSet) -> ParticleProvider<T>,
    )

}