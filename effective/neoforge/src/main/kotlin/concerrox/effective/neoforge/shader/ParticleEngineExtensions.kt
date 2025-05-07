package concerrox.effective.neoforge.shader

import net.minecraft.client.particle.ParticleEngine
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.resources.ResourceLocation

interface ParticleEngineClientExtensions {

    @Suppress("FunctionName")
    fun <O : ParticleOptions, T : ParticleType<O>> `effective$register`(
        type: ResourceLocation,
        provider: ParticleProvider<O>
    )

    @Suppress("FunctionName")
    fun <O : ParticleOptions, T : ParticleType<O>> `effective$register`(
        type: ResourceLocation,
        registration: ParticleEngine.SpriteParticleRegistration<O>
    )
}