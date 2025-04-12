package concerrox.effective.mixin.allay;

import concerrox.effective.Effective;
import concerrox.effective.EffectiveConfig;
import net.minecraft.client.renderer.entity.AllayRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.allay.Allay;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AllayRenderer.class)
public class GoldenAllayMixin {

    @Unique
    private static final ResourceLocation GOLDEN_TEXTURE = Effective.INSTANCE.id("textures/entity/golden_allay.png");

    @Shadow
    @Final
    private static ResourceLocation ALLAY_TEXTURE;

    @Inject(method = "getTextureLocation(Lnet/minecraft/world/entity/animal/allay/Allay;)Lnet/minecraft/resources/ResourceLocation;", at = @At("RETURN"), cancellable = true)
    public void getTexture(Allay allay, CallbackInfoReturnable<ResourceLocation> cir) {
        cir.setReturnValue(allay.getUUID().hashCode() % 2 == 0 && EffectiveConfig.goldenAllays.get() ? GOLDEN_TEXTURE : ALLAY_TEXTURE);
    }

}