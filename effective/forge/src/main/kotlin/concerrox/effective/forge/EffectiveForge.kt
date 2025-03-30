package concerrox.effective.forge

import concerrox.effective.Effective
import concerrox.effective.EffectiveConfig
import concerrox.effective.forge.shader.ModShaders2
import concerrox.effective.world.CascadeManager
import concerrox.effective.particle.model.SplashBottomModel
import concerrox.effective.particle.model.SplashBottomRimModel
import concerrox.effective.particle.model.SplashModel
import concerrox.effective.particle.model.SplashRimModel
import concerrox.effective.registry.ModParticles
import concerrox.effective.registry.ModSounds
import concerrox.effective.render.ModShaders
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraftforge.client.event.ClientPlayerNetworkEvent
import net.minecraftforge.client.event.EntityRenderersEvent
import net.minecraftforge.client.event.RegisterParticleProvidersEvent
import net.minecraftforge.client.event.RegisterShadersEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.forge.LOADING_CONTEXT
import thedarkcolour.kotlinforforge.forge.MOD_BUS

@Mod(Effective.MOD_ID)
object EffectiveForge {
    init {
        Effective.onInitialize()

        LOADING_CONTEXT.registerConfig(ModConfig.Type.CLIENT, EffectiveConfig.configSpec)

        DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Effective.MOD_ID).apply {
            ModParticles.PARTICLES.forEach {
                register(it.id.path, it.type::value)
            }
        }.register(MOD_BUS)

        DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Effective.MOD_ID).apply {
            ModSounds.SOUNDS.forEach {
                register(it.location.path) { it }
            }
        }.register(MOD_BUS)

        MOD_BUS.addListener { event: RegisterParticleProvidersEvent ->
            ModParticles.PARTICLES.forEach {
                @Suppress("UNCHECKED_CAST") event.registerSpriteSet(
                    it.type.value as ParticleType<ParticleOptions>,
                    it.provider as (SpriteSet) -> ParticleProvider<ParticleOptions>,
                )
            }
        }

//        MOD_BUS.addListener { event: RegisterShadersEvent ->
//            event.registerShader(
//                ShaderInstance(event.resourceProvider, Effective.id("soft_particle"), DefaultVertexFormat.PARTICLE),
//                SoftParticleRenderType::loadShader,
//            )
//        }

        MOD_BUS.addListener { event: RegisterShadersEvent ->
            val resourceManager = event.resourceProvider
            val ist = ModShaders2.PARTICLE.createInstance(resourceManager)
            ModShaders.PARTICLE = ist
            event.registerShader(ist) {}
        }

        MOD_BUS.addListener { event: EntityRenderersEvent.RegisterLayerDefinitions ->
            event.registerLayerDefinition(SplashModel.MODEL_LAYER, SplashModel.Companion::createBodyLayer)
            event.registerLayerDefinition(SplashBottomModel.MODEL_LAYER, SplashBottomModel.Companion::createBodyLayer)
            event.registerLayerDefinition(SplashRimModel.MODEL_LAYER, SplashRimModel.Companion::createBodyLayer)
            event.registerLayerDefinition(SplashBottomRimModel.MODEL_LAYER, SplashBottomRimModel.Companion::createBodyLayer)
        }

        FORGE_BUS.addListener { event: TickEvent.ClientTickEvent ->
            if (event.phase == TickEvent.Phase.END) {
                CascadeManager.tick()
            }
        }

        FORGE_BUS.addListener { _: ClientPlayerNetworkEvent.LoggingOut ->
            CascadeManager.reset()
        }
    }
}
