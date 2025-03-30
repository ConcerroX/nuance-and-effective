package concerrox.effective.mixin;

import com.mojang.blaze3d.vertex.VertexConsumer;
import concerrox.effective.world.CascadeManager;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LiquidBlockRenderer.class)
public class CascadeGenerationMixin {

    @Inject(method = "tesselate", at = @At("HEAD"))
    public void beforeTesselate(
        BlockAndTintGetter level, BlockPos pos, VertexConsumer vertexConsumer, BlockState blockState,
        FluidState fluidState, CallbackInfo ci
    ) {
        CascadeManager.INSTANCE.tryToAddCascadeGenerator(fluidState, pos.immutable());
    }

}
