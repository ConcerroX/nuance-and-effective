package concerrox.effective.forge.shader

import net.minecraft.client.particle.ParticleEngine
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraftforge.registries.RegistryObject

interface ParticleEngineClientExtensions {

    @Suppress("FunctionName")
    fun <O : ParticleOptions, T : ParticleType<O>> `effective$register`(
        type: RegistryObject<T>,
        provider: ParticleProvider<O>
    )

    @Suppress("FunctionName")
    fun <O : ParticleOptions, T : ParticleType<O>> `effective$register`(
        type: RegistryObject<T>,
        registration: ParticleEngine.SpriteParticleRegistration<O>
    )
}