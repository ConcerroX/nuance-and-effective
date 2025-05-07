package concerrox.effective.mixin.sculk;

import concerrox.effective.EffectiveConfig;
import concerrox.effective.registry.ModParticles;
import concerrox.effective.util.LevelUtils;
import concerrox.effective.util.RandomSourceUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class SculkDustSpawnMixin {

    @Inject(method = "animateTick", at = @At("HEAD"))
    protected void effective$spawnSculkParticles(
        BlockState state, Level level, BlockPos pos, RandomSource random,
        CallbackInfo ci
    ) {
        boolean p = random.nextFloat() <= (EffectiveConfig.sculkDustDensity.get() / 100F);
        if (p && state.getBlock() == Blocks.SCULK && (
            LevelUtils.isBlock(level, pos.relative(Direction.UP, 1), Blocks.SCULK_VEIN) || LevelUtils.isAir(
                level,
                pos.relative(Direction.UP, 1)
            )
        )) {
            level.addParticle(
                ModParticles.INSTANCE.getSCULK_DUST(),
                pos.getX() + 0.5 + RandomSourceUtils.nextDoubleOrNegative(random) / 3.0,
                pos.getY() + 0.975,
                pos.getZ() + 0.5 + RandomSourceUtils.nextDoubleOrNegative(random) / 3.0,
                0.0,
                0.01 + random.nextDouble() * 0.01,
                0.0
            );
        }
    }
}
