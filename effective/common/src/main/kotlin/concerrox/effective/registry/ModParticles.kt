package concerrox.effective.registry

import concerrox.effective.Effective
import concerrox.effective.particle.BubbleParticle
import concerrox.effective.particle.CascadeCloudParticle
import concerrox.effective.particle.ChorusPetalParticle
import concerrox.effective.particle.DropletParticle
import concerrox.effective.particle.EndBubbleParticle
import concerrox.effective.particle.GlowingCascadeCloudParticle
import concerrox.effective.particle.GlowingDropletParticle
import concerrox.effective.particle.GlowingRippleParticle
import concerrox.effective.particle.GlowingSplashParticle
import concerrox.effective.particle.MistParticle
import concerrox.effective.particle.RippleParticle
import concerrox.effective.particle.SplashParticle
import concerrox.effective.particle.type.SplashParticleType
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.resources.ResourceLocation
import org.ladysnake.effective.particle.FireflyParticle

object ModParticles {

    val PARTICLES = mutableListOf<ParticleEntry<*, *>>()

    val CASCADE_CLOUD by register("cascade_cloud", CascadeCloudParticle::Provider) { SimpleParticleType(true) }
    val GLOWING_CASCADE_CLOUD by register("glowing_cascade_cloud",
        GlowingCascadeCloudParticle::Provider) { SimpleParticleType(true) }
    val MIST by register("mist", MistParticle::Provider) { SimpleParticleType(true) }
    val SPLASH by register("splash", SplashParticle::Provider) { SplashParticleType(true) }
    val GLOWING_SPLASH by register("glowing_splash", GlowingSplashParticle::Provider) { SplashParticleType(true) }
    val DROPLET by register("droplet", DropletParticle::Provider) { SimpleParticleType(true) }
    val GLOWING_DROPLET by register("glowing_droplet", GlowingDropletParticle::Provider) { SimpleParticleType(true) }
    val RIPPLE by register("ripple", RippleParticle::Provider) { SimpleParticleType(true) }
    val GLOWING_RIPPLE by register("glowing_ripple", GlowingRippleParticle::Provider) { SimpleParticleType(true) }
    val BUBBLE by register("bubble", BubbleParticle::Provider) { SimpleParticleType(true) }
    val END_BUBBLE by register("end_bubble", EndBubbleParticle::Provider) { SimpleParticleType(true) }
    val FIREFLY by register("firefly", FireflyParticle::Provider) { SimpleParticleType(true) }
    val CHORUS_PETAL by register("chorus_petal", ChorusPetalParticle::Provider) { SimpleParticleType(true) }

    private fun <I : ParticleType<T>, T : ParticleOptions> register(
        id: String, provider: (SpriteSet) -> ParticleProvider<T>, type: () -> I
    ): Lazy<I> {
        val lazyType = lazy { type.invoke() }
        val entry = ParticleEntry(Effective.id(id), lazyType, provider)
        PARTICLES += entry
        return lazyType
    }

    data class ParticleEntry<I : ParticleType<T>, T : ParticleOptions>(
        val id: ResourceLocation, val type: Lazy<I>, val provider: (SpriteSet) -> ParticleProvider<T>, var extra: Any? = null
    )

}
