package concerrox.effective.mixin.glowsquid;

import com.mojang.blaze3d.vertex.PoseStack;
import concerrox.effective.Platform;
import concerrox.effective.effect.GlowSquidHypnosisManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.SquidRenderer;
import net.minecraft.world.entity.GlowSquid;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SquidRenderer.class)
public class GlowSquidHypnosisManagerMixin {

    @Inject(
        method = "setupRotations(Lnet/minecraft/world/entity/LivingEntity;Lcom/mojang/blaze3d/vertex/PoseStack;FFF)V",
        at = @At("TAIL")
    )
    protected void setupTransforms(
        LivingEntity entityLiving, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks,
        CallbackInfo ci
    ) {
        var player = Minecraft.getInstance().player;
        if (Platform.isSatinInstalled() && player != null && entityLiving instanceof GlowSquid glowSquid && glowSquid.getDarkTicksRemaining() <= 0
            && Math.sqrt(player.position().distanceToSqr(glowSquid.position())) < 20) {
            GlowSquidHypnosisManager.INSTANCE.getRENDERED_GLOW_SQUIDS().add(glowSquid);
        }
    }

}
