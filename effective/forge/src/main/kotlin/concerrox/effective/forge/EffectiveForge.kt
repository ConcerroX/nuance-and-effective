package concerrox.effective.forge

import concerrox.effective.Effective
import concerrox.effective.EffectiveConfig
import concerrox.effective.effect.CascadeManager
import concerrox.effective.effect.GlowSquidHypnosisManager
import concerrox.effective.forge.shader.ModShaders2
import concerrox.effective.forge.shader.ParticleEngineClientExtensions
import concerrox.effective.isSatinInstalled
import concerrox.effective.particle.model.SplashBottomModel
import concerrox.effective.particle.model.SplashBottomRimModel
import concerrox.effective.particle.model.SplashModel
import concerrox.effective.particle.model.SplashRimModel
import concerrox.effective.registry.ModParticles
import concerrox.effective.registry.ModSounds
import concerrox.effective.render.ModShaders
import concerrox.effective.screenshake.ScreenShakeManager
import ladysnake.satin.api.event.EntitiesPreRenderCallback
import ladysnake.satin.api.event.ShaderEffectRenderCallback
import net.minecraft.client.Minecraft
import net.minecraft.client.particle.ParticleEngine
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.core.Registry
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraft.resources.ResourceKey
import net.minecraftforge.client.event.ClientPlayerNetworkEvent
import net.minecraftforge.client.event.EntityRenderersEvent
import net.minecraftforge.client.event.RegisterParticleProvidersEvent
import net.minecraftforge.client.event.RegisterShadersEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.IForgeRegistry
import net.minecraftforge.registries.RegistryBuilder
import net.minecraftforge.registries.RegistryObject
import thedarkcolour.kotlinforforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.forge.LOADING_CONTEXT
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import java.util.function.Supplier


@Mod(Effective.MOD_ID)
object EffectiveForge {


    private val PARTICLE_RESOURCE_KEY: ResourceKey<Registry<ParticleType<*>>> = ResourceKey.createRegistryKey(
        Effective.id("particle_type"))
    private val PARTICLE_REGISTRY: DeferredRegister<ParticleType<*>> = DeferredRegister.create(PARTICLE_RESOURCE_KEY,
        Effective.MOD_ID)

    // Disable the syncing of the registry to prevent "unregistered object on dedicated server"
    internal val PARTICLE_REGISTRY_CLIENT: Supplier<IForgeRegistry<ParticleType<*>>> = PARTICLE_REGISTRY.makeRegistry {
        RegistryBuilder<ParticleType<*>>().disableSaving().disableSync()
    }

    init {
        Effective.onInitialize()
        ModAmbientConditionsForge.initialize()

        LOADING_CONTEXT.registerConfig(ModConfig.Type.CLIENT, EffectiveConfig.configSpec)

        PARTICLE_REGISTRY.apply {
            ModParticles.PARTICLES.forEach {
                it.extra = register(it.id.path, it.type::value)
            }
        }.register(MOD_BUS)

        DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Effective.MOD_ID).apply {
            ModSounds.SOUNDS.forEach {
                register(it.location.path) { it }
            }
        }.register(MOD_BUS)

        MOD_BUS.addListener { _: RegisterParticleProvidersEvent ->
            fun <O : ParticleOptions, T : ParticleType<O>> register(
                type: RegistryObject<T>,
                registration: ParticleEngine.SpriteParticleRegistration<O>,
            ) = (Minecraft.getInstance().particleEngine as ParticleEngineClientExtensions).`effective$register`(type,
                registration)

            ModParticles.PARTICLES.forEach {
                @Suppress("UNCHECKED_CAST") register(
                    it.extra as RegistryObject<ParticleType<ParticleOptions>>,
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
            event.registerLayerDefinition(SplashBottomRimModel.MODEL_LAYER,
                SplashBottomRimModel.Companion::createBodyLayer)
        }

        FORGE_BUS.addListener { event: TickEvent.ClientTickEvent ->
            if (event.phase == TickEvent.Phase.END) {
                CascadeManager.tick()
                ScreenShakeManager.tick()
            }
        }

        FORGE_BUS.addListener { _: ClientPlayerNetworkEvent.LoggingOut ->
            CascadeManager.reset()
        }

        if (isSatinInstalled()) {
            GlowSquidHypnosisManager.RAINBOW_SHADER // Force to load the shader

            FORGE_BUS.addListener { event: TickEvent.ClientTickEvent ->
                if (event.phase == TickEvent.Phase.END) {
                    GlowSquidHypnosisManager.rainbowTimer++
                }
            }

            FORGE_BUS.addListener { event: ShaderEffectRenderCallback ->
                GlowSquidHypnosisManager.onShaderEffectRendered(event.tickDelta)
            }

            FORGE_BUS.addListener { event: EntitiesPreRenderCallback ->
                GlowSquidHypnosisManager.rainbowSTime.set(
                    (GlowSquidHypnosisManager.rainbowTimer + event.tickDelta) * 0.05F)
            }
        }
    }
}
