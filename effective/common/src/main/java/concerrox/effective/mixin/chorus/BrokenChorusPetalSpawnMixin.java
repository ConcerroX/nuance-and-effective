package concerrox.effective.mixin.chorus;

import concerrox.effective.EffectiveConfig;
import concerrox.effective.registry.ModParticles;
import concerrox.effective.util.RandomSourceUtils;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChorusFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleEngine.class)
public abstract class BrokenChorusPetalSpawnMixin {

    @Shadow
    @Final
    private RandomSource random;

    @Shadow
    public abstract Particle createParticle(
        ParticleOptions particleData, double x, double y, double z, double xSpeed,
        double ySpeed, double zSpeed
    );

    @Inject(method = "destroy", at = @At(value = "RETURN"))
    public void addBlockBreakParticles(BlockPos pos, BlockState state, CallbackInfo ci) {
        if (state.getBlock() == Blocks.CHORUS_FLOWER) {
            int density = (int) (
                (6 - state.getValue(ChorusFlowerBlock.AGE)) * (
                    EffectiveConfig.chorusPetalDensity.get() * 10
                )
            );
            for (int i = 0; i < density; i++) {
                createParticle(
                    ModParticles.INSTANCE.getCHORUS_PETAL(),
                    pos.getX() + 0.5,
                    pos.getY() + 0.5,
                    pos.getZ() + 0.5,
                    RandomSourceUtils.nextDoubleOrNegative(random) / 10,
                    RandomSourceUtils.nextDoubleOrNegative(random) / 10,
                    RandomSourceUtils.nextDoubleOrNegative(random) / 10
                );
            }
        }
    }
}
