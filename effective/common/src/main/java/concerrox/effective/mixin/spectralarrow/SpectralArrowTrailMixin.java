package concerrox.effective.mixin.spectralarrow;

import com.mojang.blaze3d.vertex.PoseStack;
import concerrox.effective.EffectiveConfig;
import concerrox.effective.registry.ModParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.SpectralArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(ArrowRenderer.class)
public abstract class SpectralArrowTrailMixin<T extends AbstractArrow> extends EntityRenderer<T> {

    protected SpectralArrowTrailMixin(EntityRendererProvider.Context context) {
        super(context);
    }

    @Inject(
        method = "render(Lnet/minecraft/world/entity/projectile/AbstractArrow;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
        at = @At("TAIL")
    )
    public void render(
        T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight,
        CallbackInfo ci
    ) {
        // new render
        if (EffectiveConfig.spectralArrowTrails.get() != EffectiveConfig.TrailOptions.NONE
            && entity instanceof SpectralArrow spectralArrow && !spectralArrow.isInvisible()) {
            Color data = new Color(0xFFFF77);
            // trail
            if (EffectiveConfig.spectralArrowTrails.get() == EffectiveConfig.TrailOptions.BOTH
                || EffectiveConfig.spectralArrowTrails.get() == EffectiveConfig.TrailOptions.TRAIL) {
                //                matrixStack.push();
                //                List<TrailPoint> positions = ((PositionTrackedEntity) spectralArrow).getPastPositions();
                //                VFXBuilders.WorldVFXBuilder builder = VFXBuilders.createWorld().setRenderType(getTrailRenderType());
                //
                //                float size = 0.2f;
                //                float alpha = 1f;
                //
                //                float x = (float) MathHelper.lerp(tickDelta, spectralArrow.prevX, spectralArrow.getX());
                //                float y = (float) MathHelper.lerp(tickDelta, spectralArrow.prevY, spectralArrow.getY());
                //                float z = (float) MathHelper.lerp(tickDelta, spectralArrow.prevZ, spectralArrow.getZ());
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
            if (EffectiveConfig.spectralArrowTrails.get() == EffectiveConfig.TrailOptions.BOTH
                || EffectiveConfig.spectralArrowTrails.get() == EffectiveConfig.TrailOptions.TWINKLE) {
                if ((spectralArrow.level().random.nextInt(100) + 1) <= 5 && !Minecraft.getInstance().isPaused()) {
                    float spreadDivider = 4f;
                    var particle = ModParticles.INSTANCE.getALLAY_TWINKLE();
                    particle.setColor(data);
                    particle.setScale(0.06F);
                    var random = spectralArrow.level().random;
                    var p = spectralArrow.getLightProbePosition(Minecraft.getInstance().getFrameTime());
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
