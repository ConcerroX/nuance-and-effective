package concerrox.effective.forge

import com.mojang.blaze3d.vertex.DefaultVertexFormat
import concerrox.effective.Effective
import concerrox.effective.registry.ModParticles
import concerrox.effective.shader.SoftParticleRenderType
import net.minecraft.client.gui.screens.OptionsScreen
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.particle.ParticleProvider
import net.minecraft.client.particle.SpriteSet
import net.minecraft.client.renderer.ShaderInstance
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.core.particles.ParticleType
import net.minecraftforge.client.ConfigScreenHandler
import net.minecraftforge.client.event.RegisterParticleProvidersEvent
import net.minecraftforge.client.event.RegisterShadersEvent
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.FORGE_BUS
import thedarkcolour.kotlinforforge.forge.LOADING_CONTEXT
import thedarkcolour.kotlinforforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.forge.MOD_CONTEXT


@Mod(Effective.MOD_ID)
object EffectiveForge {

    init {
        Effective.onInitialize()

        DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Effective.MOD_ID).apply {
            ModParticles.PARTICLES.forEach {
                register(it.id.path, it.type::value)
            }
        }.register(MOD_BUS)

        MOD_BUS.addListener { event: RegisterParticleProvidersEvent ->
            ModParticles.PARTICLES.forEach {
                @Suppress("UNCHECKED_CAST") event.registerSpriteSet(it.type.value as ParticleType<ParticleOptions>,
                    it.provider as (SpriteSet) -> ParticleProvider<ParticleOptions>)
            }
        }

        MOD_BUS.addListener { event: RegisterShadersEvent ->
            event.registerShader(
                ShaderInstance(event.resourceProvider, Effective.id("soft_particle"), DefaultVertexFormat.PARTICLE),
                SoftParticleRenderType::loadShader)
        }

//        LOADING_CONTEXT.registerConfig(ModConfig.Type.CLIENT, Config.Client.SPEC)

//        MOD_BUS.addListener { event: FMLClientSetupEvent ->
//            LOADING_CONTEXT.registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory::class.java) {
//                ConfigScreenHandler.ConfigScreenFactory { minecraft, prevScreen ->
//                    OptionsScreen(prevScreen, null)
//                }
//            }
//        }

    }


}