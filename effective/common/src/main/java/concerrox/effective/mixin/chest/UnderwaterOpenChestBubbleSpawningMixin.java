package concerrox.effective.mixin.chest;

import concerrox.effective.EffectiveConfig;
import concerrox.effective.effect.ChestBubbleSpawner;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChestBlockEntity.class)
public class UnderwaterOpenChestBubbleSpawningMixin<T extends BlockEntity & LidBlockEntity> {

    @Inject(method = "lidAnimateTick", at = @At("TAIL"))
    private static void clientTick(
        Level level, BlockPos pos, BlockState state, ChestBlockEntity blockEntity,
        CallbackInfo ci
    ) {
        boolean bl = level != null;
        if (EffectiveConfig.underwaterOpenChestBubbles.get() && bl && level.random.nextInt(2) == 0) {
            ChestBubbleSpawner.INSTANCE.doUnderwaterChestLogic(level, blockEntity);
        }
    }

}