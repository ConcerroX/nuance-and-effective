package concerrox.effective.mixin.chorus;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public abstract class ChorusFlowerBlockMixin {

    @Inject(method = "animateTick", at = @At("TAIL"))
    protected void effective$afterAnimateTick(
        BlockState state, Level level, BlockPos pos, RandomSource random,
        CallbackInfo ci
    ) {
    }

}
