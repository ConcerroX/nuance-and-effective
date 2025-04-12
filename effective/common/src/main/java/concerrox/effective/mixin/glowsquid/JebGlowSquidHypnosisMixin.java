package concerrox.effective.mixin.glowsquid;

import concerrox.effective.Platform;
import concerrox.effective.effect.GlowSquidHypnosisManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.GlowSquid;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public class JebGlowSquidHypnosisMixin {

    @Inject(method = "getRenderType", at = @At("RETURN"), cancellable = true)
    protected void getRenderLayer(
        LivingEntity livingEntity, boolean bodyVisible, boolean translucent, boolean glowing,
        CallbackInfoReturnable<RenderType> cir
    ) {
        if (Platform.isSatinInstalled() && livingEntity instanceof GlowSquid) {
            RenderType base = cir.getReturnValue();
            if (livingEntity.hasCustomName() && "jeb_".equals(livingEntity.getName().getString())) {
                cir.setReturnValue(
                    base == null ? null : GlowSquidHypnosisManager.INSTANCE.getRainbowShaderRenderType$effective_common(
                        base));
            }
        }
    }

}
