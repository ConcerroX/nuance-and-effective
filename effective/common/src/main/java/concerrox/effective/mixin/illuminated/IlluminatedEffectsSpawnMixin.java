package concerrox.effective.mixin.illuminated;

import concerrox.effective.EffectiveConfig;
import concerrox.effective.effect.IlluminatedEffectsSpawner;
import concerrox.effective.registry.ModParticles;
import concerrox.effective.util.RandomSourceUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.LocalDate;
import java.time.Month;
import java.util.function.Supplier;

@Mixin(ClientLevel.class)
public abstract class IlluminatedEffectsSpawnMixin extends Level {

    protected IlluminatedEffectsSpawnMixin(
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

    @Inject(
        method = "doAnimateTick", slice = @Slice(
        from = @At(
            value = "INVOKE", target = "Lnet/minecraft/world/level/biome/Biome;getAmbientParticle()Ljava/util/Optional;"
        )
    ), at = @At(
        value = "INVOKE",
        target = "Ljava/util/Optional;ifPresent(Ljava/util/function/Consumer;)V",
        ordinal = 0,
        shift = At.Shift.AFTER
    )
    )
    private void effective$spawnEffectsFromRandomBlockDisplayTicks(
        int posX, int posY, int posZ, int range, RandomSource random, Block block, BlockPos.MutableBlockPos blockPos,
        CallbackInfo ci
    ) {
        BlockPos.MutableBlockPos pos = blockPos.offset(
            Mth.floor(RandomSourceUtils.nextDoubleOrNegative(random) * 50),
            Mth.floor(RandomSourceUtils.nextDoubleOrNegative(random) * 10),
            Mth.floor(RandomSourceUtils.nextDoubleOrNegative(random) * 50)
        ).mutable();
        //        BlockPos.MutableBlockPos pos2 = pos.mutable();
        var biome = getBiome(pos);

        if (EffectiveConfig.fireflyDensity.get() > 0) {
            IlluminatedEffectsSpawner.INSTANCE.trySpawnFireflies(this, blockPos, random);
        }

        // WILL O' WISP
        pos = blockPos.offset(
            Mth.floor(RandomSourceUtils.nextDoubleOrNegative(random) * 50),
            Mth.floor(RandomSourceUtils.nextDoubleOrNegative(random) * 25),
            Mth.floor(RandomSourceUtils.nextDoubleOrNegative(random) * 50)
        ).mutable();
        if (EffectiveConfig.willOWispDensity.get() > 0) {
            if (biome.is(Biomes.SOUL_SAND_VALLEY)) {
                if (random.nextFloat() * 100f <= 0.01F * EffectiveConfig.willOWispDensity.get()) {
                    if (getBlockState(pos).is(BlockTags.SOUL_FIRE_BASE_BLOCKS)) {
                        addParticle(
                            ModParticles.INSTANCE.getWILL_O_WISP(),
                            pos.getX(),
                            pos.getY(),
                            pos.getZ(),
                            0,
                            0,
                            0
                        );
                    }
                }
            }
        }

        // EYES IN THE DARK
        if ((
            EffectiveConfig.eyesInTheDark.get() == EffectiveConfig.EyesInTheDarkOptions.ALWAYS || (
                EffectiveConfig.eyesInTheDark.get() == EffectiveConfig.EyesInTheDarkOptions.HALLOWEEN
                    && LocalDate.now().getMonth() == Month.OCTOBER
            )
        ) && random.nextFloat() <= 0.00002F) {
            addParticle(
                ModParticles.INSTANCE.getEYES(),
                pos.getX() + 0.5,
                pos.getY() + 0.5,
                pos.getZ() + 0.5,
                0.0,
                0.0,
                0.0
            );
        }
    }

}
