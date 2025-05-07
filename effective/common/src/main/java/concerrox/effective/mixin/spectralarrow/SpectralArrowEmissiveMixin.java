package concerrox.effective.mixin.spectralarrow;

import concerrox.effective.EffectiveConfig;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.SpectralArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public class SpectralArrowEmissiveMixin<T extends Entity> {

    @Inject(method = "getBlockLightLevel", at = @At("RETURN"), cancellable = true)
    protected void getBlockLightLevel(T entity, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        if (EffectiveConfig.spectralArrowTrails.get() != EffectiveConfig.TrailOptions.NONE
            && entity instanceof SpectralArrow) {
            cir.setReturnValue(15);
        }
    }

}
