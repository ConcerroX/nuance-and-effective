package concerrox.effective.mixin.glowsquid;

import com.mojang.blaze3d.vertex.PoseStack;
import concerrox.effective.Platform;
import concerrox.effective.effect.GlowSquidHypnosisManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.SquidRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.GlowSquid;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Squid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SquidRenderer.class)
public class GlowSquidHypnosisManagerMixin<T extends Squid> {

    @Inject(
        method = "setupRotations(Lnet/minecraft/world/entity/animal/Squid;Lcom/mojang/blaze3d/vertex/PoseStack;FFFF)V",
        at = @At("TAIL")
    )
    protected void setupTransforms(
        T entity, PoseStack poseStack, float bob, float yBodyRot, float partialTick, float scale, CallbackInfo ci
    ) {
        var player = Minecraft.getInstance().player;
        if (Platform.isSatinInstalled() && player != null && entity instanceof GlowSquid glowSquid && glowSquid.getDarkTicksRemaining() <= 0
            && Math.sqrt(player.position().distanceToSqr(glowSquid.position())) < 20) {
            GlowSquidHypnosisManager.INSTANCE.getRENDERED_GLOW_SQUIDS().add(glowSquid);
        }
    }

}
