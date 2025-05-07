package concerrox.effective.neoforge.mixin;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(SimpleParticleType.class)
public abstract class SimpleParticleTypeMixin extends ParticleType<SimpleParticleType> {

    protected SimpleParticleTypeMixin(boolean overrideLimitter) {
        super(overrideLimitter);
    }

//    @ModifyExpressionValue(
//            method = "writeToString", at = @At(
//            value = "INVOKE",
//            target = "Lnet/minecraft/core/Registry;getKey(Ljava/lang/Object;)Lnet/minecraft/resources/ResourceLocation;"
//    )
//    )
//    private ResourceLocation remapParticles(
//            ResourceLocation original
//    ) {
//        var clientRegistry = EffectiveForge.INSTANCE.getPARTICLE_REGISTRY_CLIENT$effective_forge().get();
//        if (clientRegistry != null) {
//            return original == null ? clientRegistry.getKey(this) : original;
//        } else {
//            return original;
//        }
//    }
}
