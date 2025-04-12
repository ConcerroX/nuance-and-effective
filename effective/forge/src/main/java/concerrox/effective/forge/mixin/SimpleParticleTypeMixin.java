package concerrox.effective.forge.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import concerrox.effective.forge.EffectiveForge;
import concerrox.effective.registry.ModParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SimpleParticleType.class)
public abstract class SimpleParticleTypeMixin extends ParticleType<SimpleParticleType> {

    public SimpleParticleTypeMixin(
            boolean pOverrideLimiter,
            @SuppressWarnings("deprecation") ParticleOptions.Deserializer<SimpleParticleType> pDeserializer
    ) {
        super(pOverrideLimiter, pDeserializer);
    }

    @ModifyExpressionValue(
            method = "writeToString", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/Registry;getKey(Ljava/lang/Object;)Lnet/minecraft/resources/ResourceLocation;"
    )
    )
    private ResourceLocation remapParticles(
            ResourceLocation original
    ) {
        var clientRegistry = EffectiveForge.INSTANCE.getPARTICLE_REGISTRY_CLIENT$effective_forge().get();
        if (clientRegistry != null) {
            return original == null ? clientRegistry.getKey(this) : original;
        } else {
            return original;
        }
    }
}
