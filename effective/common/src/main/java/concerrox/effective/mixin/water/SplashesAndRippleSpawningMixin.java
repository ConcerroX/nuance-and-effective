package concerrox.effective.mixin.water;

import concerrox.effective.EffectiveConfig;
import concerrox.effective.util.LevelUtils;
import concerrox.effective.util.RandomSourceUtils;
import concerrox.effective.util.WaterUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.WaterFluid;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WaterFluid.class)
public class SplashesAndRippleSpawningMixin {

    @Unique
    private static boolean effective$canSpawnSplashes(Level level, BlockPos pos) {
        if (EffectiveConfig.dropletSplashingDensity.get() > 0) {
            var fluidState = level.getFluidState(pos);
            if (!fluidState.isSource() & fluidState.getOwnHeight() >= 0.77) {
                var mutable = new BlockPos.MutableBlockPos();
                for (var dir : Direction.values()) {
                    if (dir != Direction.DOWN && LevelUtils.isAir(level, mutable.setWithOffset(pos, dir))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Unique
    private static boolean effective$canSpawnRipple(Level level, BlockPos pos) {
        return EffectiveConfig.rainRippleDensity.get() > 0 && level.getFluidState(pos).isSource() && level.isRaining()
            && LevelUtils.isAir(level, pos.offset(0, 1, 0));
    }

    @Inject(method = "animateTick", at = @At("HEAD"))
    protected void effective$splashAndRainRipples(
        Level level, BlockPos pos, FluidState state, RandomSource random,
        CallbackInfo ci
    ) {
        if (effective$canSpawnSplashes(level, pos.above())) {
            var flowVelocity = state.getFlow(level, pos);
            for (int i = 0; i <= random.nextInt(EffectiveConfig.dropletSplashingDensity.get()); i++) {
                level.addParticle(
                    ParticleTypes.SPLASH,
                    pos.getX() + 0.5 + RandomSourceUtils.nextDoubleOrNegative(random) / 2.0,
                    pos.getY() + 1.0 + random.nextFloat(),
                    pos.getZ() + 0.5 + RandomSourceUtils.nextDoubleOrNegative(random) / 2.0,
                    flowVelocity.x * random.nextFloat(),
                    random.nextFloat() / 10f,
                    flowVelocity.z * random.nextFloat()
                );
            }
        }

        if (effective$canSpawnRipple(level, pos)) {
            if (random.nextInt(10) <= EffectiveConfig.rainRippleDensity.get()) {
                if (level.getBiome(pos).value().getPrecipitationAt(pos) == Biome.Precipitation.RAIN
                    && level.canSeeSkyFromBelowWater(pos)) {
                    WaterUtils.INSTANCE.spawnWaterEffect(
                        level,
                        Vec3.atCenterOf(pos).add(
                            random.nextFloat() - random.nextFloat(),
                            .39f,
                            random.nextFloat() - random.nextFloat()
                        ),
                        0f,
                        0f,
                        0f,
                        WaterUtils.WaterEffectType.RIPPLE
                    );
                }
            }
        }
    }

}
