package concerrox.effective.mixin.allay;

import com.mojang.blaze3d.vertex.PoseStack;
import concerrox.effective.EffectiveConfig;
import concerrox.effective.registry.ModParticles;
import concerrox.effective.util.AllayUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.allay.Allay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(LivingEntityRenderer.class)
public abstract class AllayTrailMixin<T extends LivingEntity> extends EntityRenderer<T> {

    protected AllayTrailMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @Inject(
        method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
        at = @At("TAIL")
    )
    public void render(
        T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight,
        CallbackInfo ci
    ) {
        // new render
        if (EffectiveConfig.allayTrails.get() != EffectiveConfig.TrailOptions.NONE
            && entity instanceof Allay allayEntity && !allayEntity.isInvisible()) {
            Color data = new Color(
                allayEntity.getUUID().hashCode() % 2 == 0 && EffectiveConfig.goldenAllays.get() ? 0xFFC200 : 0x22CFFF);
            // trail
            if (EffectiveConfig.allayTrails.get() == EffectiveConfig.TrailOptions.BOTH
                || EffectiveConfig.allayTrails.get() == EffectiveConfig.TrailOptions.TRAIL) {
                //                matrixStack.push();
                //                List<TrailPoint> positions = ((PositionTrackedEntity) allayEntity).getPastPositions();
                //                VFXBuilders.WorldVFXBuilder builder = VFXBuilders.createWorld().setRenderType(getTrailRenderType());
                //
                //                float size = 0.2f;
                //                float alpha = 1f;
                //
                //                float x = (float) MathHelper.lerp(tickDelta, allayEntity.prevX, allayEntity.getX());
                //                float y = (float) MathHelper.lerp(tickDelta, allayEntity.prevY, allayEntity.getY());
                //                float z = (float) MathHelper.lerp(tickDelta, allayEntity.prevZ, allayEntity.getZ());
                //
                //                matrixStack.translate(-x, -y, -z);
                //                builder.setColor(new Color(data.color))
                //                    .setAlpha(alpha)
                //                    .renderTrail(matrixStack,
                //                        positions,
                //                        f -> MathHelper.sqrt(f) * size,
                //                        f -> builder.setAlpha((float) Math.cbrt(Math.max(0, (alpha * f) - 0.1f)))
                //                    )
                //                    .setAlpha(alpha)
                //                    .renderTrail(matrixStack,
                //                        positions,
                //                        f -> (MathHelper.sqrt(f) * size) / 1.5f,
                //                        f -> builder.setAlpha((float) Math.cbrt(Math.max(0, (((alpha * f) / 1.5f) - 0.1f))))
                //                    );
                //
                //                matrixStack.pop();
            }

            // twinkles
            if (EffectiveConfig.allayTrails.get() == EffectiveConfig.TrailOptions.BOTH
                || EffectiveConfig.allayTrails.get() == EffectiveConfig.TrailOptions.TWINKLE) {
                if ((allayEntity.getRandom().nextInt(100) + 1) <= 5 && AllayUtils.INSTANCE.isGoingFast(allayEntity)
                    && !Minecraft.getInstance().isPaused()) {
                    float spreadDivider = 4f;
                    var particle = ModParticles.INSTANCE.getALLAY_TWINKLE();
                    particle.setColor(data);
                    var random = allayEntity.getRandom();
                    var p = allayEntity.getLightProbePosition(Minecraft.getInstance().getFrameTime());
                    entity.level().addParticle(
                        particle,
                        p.x + random.nextGaussian() / spreadDivider,
                        p.y - 0.2f + random.nextGaussian() / spreadDivider,
                        p.z + random.nextGaussian() / spreadDivider,
                        0.0,
                        0.05,
                        0.0
                    );
                }
            }
        }
    }

}
