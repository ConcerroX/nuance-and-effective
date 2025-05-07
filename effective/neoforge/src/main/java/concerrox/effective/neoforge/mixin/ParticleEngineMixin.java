package concerrox.effective.neoforge.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import concerrox.effective.neoforge.EffectiveNeoForge;
import concerrox.effective.neoforge.shader.ParticleEngineClientExtensions;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Map;

@Mixin(ParticleEngine.class)
public class ParticleEngineMixin implements ParticleEngineClientExtensions {

    @Shadow
    @Final
    private Map<ResourceLocation, ParticleProvider<?>> providers;

    @Shadow
    @Final
    private Map<ResourceLocation, ParticleEngine.MutableSpriteSet> spriteSets;

    @Override
    public <O extends ParticleOptions, T extends ParticleType<O>> void effective$register(
        @NotNull ResourceLocation type, @NotNull ParticleProvider<O> provider) {
        providers.put(type, provider);
    }

    @Override
    public <O extends ParticleOptions, T extends ParticleType<O>> void effective$register(
        @NotNull ResourceLocation type, @NotNull ParticleEngine.SpriteParticleRegistration<O> registration) {
        ParticleEngine.MutableSpriteSet spriteSet = new ParticleEngine.MutableSpriteSet();
        spriteSets.put(type, spriteSet);
        providers.put(type, registration.create(spriteSet));
    }

    @ModifyExpressionValue(
        method = "makeParticle", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/core/Registry;getKey(Ljava/lang/Object;)Lnet/minecraft/resources/ResourceLocation;"
    )
    )
    private <T extends ParticleOptions> ResourceLocation remapParticles(
        ResourceLocation original,
        @Local(ordinal = 0, argsOnly = true) T options
    ) {
        var clientRegistry = EffectiveNeoForge.Companion.getPARTICLE_REGISTRY_CLIENT$effective_neoforge();
        return original == null ? clientRegistry.getKey(options.getType()) : original;
    }

}