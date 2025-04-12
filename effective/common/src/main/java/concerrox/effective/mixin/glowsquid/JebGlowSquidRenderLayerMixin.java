package concerrox.effective.mixin.glowsquid;

import com.mojang.blaze3d.vertex.PoseStack;
import concerrox.effective.effect.GlowSquidHypnosisManager;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.GlowSquid;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderLayer.class)
public abstract class JebGlowSquidRenderLayerMixin {

//    @Unique
//    private static boolean effective$isRgb;
//
//    @Inject(method = "renderColoredCutoutModel", at = @At("HEAD"))
//    private static <T extends LivingEntity> void captureEntity(
//        EntityModel<T> model, ResourceLocation textureLocation, PoseStack poseStack, MultiBufferSource buffer,
//        int packedLight, T entity, float red, float green, float blue, CallbackInfo ci
//    ) {
//        effective$isRgb = entity instanceof GlowSquid && entity.hasCustomName() && "jeb_".equals(entity.getName().getString());
//    }
//
//    @ModifyArg(method = "renderColoredCutoutModel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/MultiBufferSource;getBuffer(Lnet/minecraft/client/renderer/RenderType;)Lcom/mojang/blaze3d/vertex/VertexConsumer;"))
//    private static RenderType replaceRenderType(RenderType renderType) {
//        if (effective$isRgb) {
//            return GlowSquidHypnosisManager.INSTANCE.getRAINBOW_SHADER().getRenderLayer(renderType);
//        }
//        return renderType;
//    }
}