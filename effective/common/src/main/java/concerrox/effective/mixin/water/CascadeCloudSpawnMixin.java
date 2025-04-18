package concerrox.effective.mixin.water;

import concerrox.effective.EffectiveConfig;
import concerrox.effective.effect.CascadeManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

@Mixin(Level.class)
public abstract class CascadeCloudSpawnMixin {

    @Shadow
    public abstract FluidState getFluidState(BlockPos pos);

    @Inject(
        method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z",
        at = @At("RETURN")
    )
    private void effective$flowingWaterCascade(
        BlockPos pos, BlockState state, int flags, int recursionLeft,
        CallbackInfoReturnable<Boolean> cir
    ) {
        if (cir.getReturnValueZ() && EffectiveConfig.flowingWaterSpawnCascade.get()
            && getFluidState(pos).getType() == Fluids.FLOWING_WATER) {
            CascadeManager.INSTANCE.spawnCascadeCloud(
                Level.class.cast(this),
                new CascadeManager.Cascade(pos, getFluidState(pos).getOwnHeight() / 2f, true, new Color(0xFFFFFF))
            );
        }
    }

}
