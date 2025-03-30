package concerrox.effective.registry

import concerrox.effective.Effective
import concerrox.effective.particle.CascadeCloudParticle
import concerrox.effective.particle.GlowingCascadeCloudParticle
import concerrox.effective.particle.GlowingSplashParticle
import concerrox.effective.particle.MistParticle
import concerrox.effective.particle.SplashParticle
import concerrox.effective.particle.type.SplashParticleType
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.resources.ResourceLocation

object ModParticles {

    val PARTICLES = mutableListOf<ParticleEntry<*, *>>()

    val CASCADE_CLOUD by register("cascade_cloud", CascadeCloudParticle::Provider) { SimpleParticleType(true) }
    val GLOWING_CASCADE_CLOUD by register("glowing_cascade_cloud",
        GlowingCascadeCloudParticle::Provider) { SimpleParticleType(true) }
    val MIST by register("mist", MistParticle::Provider) { SimpleParticleType(true) }
    val SPLASH by register("splash", SplashParticle::Provider) { SplashParticleType(true) }
    val GLOWING_SPLASH by register("glowing_splash", GlowingSplashParticle::Provider) { SplashParticleType(true) }

    private fun <I : ParticleType<T>, T : ParticleOptions> register(
        id: String, provider: (SpriteSet) -> ParticleProvider<T>, type: () -> I
    ): Lazy<I> {
        val lazyType = lazy { type.invoke() }
        val entry = ParticleEntry(Effective.id(id), lazyType, provider)
        PARTICLES += entry
        return lazyType
    }

    data class ParticleEntry<I : ParticleType<T>, T : ParticleOptions>(
        val id: ResourceLocation, val type: Lazy<I>, val provider: (SpriteSet) -> ParticleProvider<T>
    )

}
