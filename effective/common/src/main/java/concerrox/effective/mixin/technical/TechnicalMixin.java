package concerrox.effective.mixin.technical;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import concerrox.effective.EffectiveConfig;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import java.util.function.Supplier;

@Mixin(ClientLevel.class)
public abstract class TechnicalMixin extends Level {

    protected TechnicalMixin(
        WritableLevelData levelData, ResourceKey<Level> dimension, RegistryAccess registryAccess,
        Holder<DimensionType> dimensionTypeRegistration, Supplier<ProfilerFiller> profiler, boolean isClientSide,
        boolean isDebug, long biomeZoomSeed, int maxChainedNeighborUpdates
    ) {
        super(
            levelData,
            dimension,
            registryAccess,
            dimensionTypeRegistration,
            profiler,
            isClientSide,
            isDebug,
            biomeZoomSeed,
            maxChainedNeighborUpdates
        );
    }

    @ModifyConstant(method = "animateTick", constant = @Constant(intValue = 667), require = 0)
    public int effective$multiplyRandomBlockDisplayTicksFrequency(int constant) {
        return (int) Math.round(667 * EffectiveConfig.randomBlockDisplayTicksFrequencyMultiplier.get());
    }

    @ModifyConstant(method = "animateTick", constant = @Constant(intValue = 16), require = 0)
    public int effective$overwriteRandomBlockDisplayTicksDistanceClose(int constant) {
        return EffectiveConfig.randomBlockDisplayTicksDistanceClose.get();
    }

    @ModifyConstant(method = "animateTick", constant = @Constant(intValue = 32), require = 0)
    public int effective$overwriteRandomBlockDisplayTicksDistanceFar(int constant) {
        return EffectiveConfig.randomBlockDisplayTicksDistanceFar.get();
    }

    @WrapWithCondition(
        method = "animateTick", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/client/multiplayer/ClientLevel;doAnimateTick(IIIILnet/minecraft/util/RandomSource;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/core/BlockPos$MutableBlockPos;)V",
        ordinal = 0
    )
    )
    public boolean effective$cancelRandomBlockDisplayTicksClose(
        ClientLevel instance, int posX, int posY, int posZ, int range, RandomSource random, Block block,
        BlockPos.MutableBlockPos blockPos
    ) {
        return EffectiveConfig.randomBlockDisplayTicksDistanceClose.get() > 0;
    }

    @WrapWithCondition(
        method = "animateTick", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/client/multiplayer/ClientLevel;doAnimateTick(IIIILnet/minecraft/util/RandomSource;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/core/BlockPos$MutableBlockPos;)V",
        ordinal = 1
    )
    )
    public boolean effective$cancelRandomBlockDisplayTicksFar(
        ClientLevel instance, int posX, int posY, int posZ, int range, RandomSource random, Block block,
        BlockPos.MutableBlockPos blockPos
    ) {
        return EffectiveConfig.randomBlockDisplayTicksDistanceFar.get() > 0;
    }

}
