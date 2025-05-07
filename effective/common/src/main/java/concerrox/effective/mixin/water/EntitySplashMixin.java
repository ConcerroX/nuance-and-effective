package concerrox.effective.mixin.water;

import concerrox.effective.EffectiveConfig;
import concerrox.effective.effect.SplashSpawner;
import concerrox.effective.registry.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntitySplashMixin {
    @Shadow
    private Level level;

    @Shadow
    protected boolean wasTouchingWater;

    @Shadow
    public abstract boolean onGround();

    @Shadow
    public abstract boolean isInLava();

    @Shadow
    public abstract BlockPos blockPosition();

    @Shadow
    private Vec3 deltaMovement;

    @Shadow
    public abstract boolean isVehicle();

    @Shadow
    @Nullable
    public abstract Entity getFirstPassenger();

    @Shadow
    public abstract double getX();

    @Shadow
    public abstract double getZ();

    @Shadow
    public abstract double getY();

    @Inject(method = "doWaterSplashEffect", at = @At("TAIL"))
    protected void onSwimmingStart(CallbackInfo callbackInfo) {
        if (level.isClientSide && EffectiveConfig.isSplashesEnabled.get()) {
            SplashSpawner.INSTANCE.trySpawnSplash((Entity) (Object) this);
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo callbackInfo) {
        if (!onGround() && !wasTouchingWater && !isInLava()
            && level.getBlockState(BlockPos.containing(
            blockPosition().getX() + deltaMovement.x,
            blockPosition().getY() + deltaMovement.y,
            blockPosition().getZ() + deltaMovement.z
        )).getBlock() == Blocks.LAVA && EffectiveConfig.isLavaSplashesEnabled.get()) {
            if (level.isClientSide) {
                var entity = isVehicle() && getFirstPassenger() != null ? getFirstPassenger() : (Entity) (Object) this;
                float f = entity == (Object) this ? 0.2f : 0.9f;
                var vec3d = entity.getDeltaMovement();
                float g = Math.min(
                    1.0f,
                    (float) Math.sqrt(
                        vec3d.x * vec3d.x * (double) 0.2f + vec3d.y * vec3d.y + vec3d.z * vec3d.z * (double) 0.2f) * f
                );
                //                System.out.println(g);
                if (g > 0.05f) {
                    for (int i = -10; i < 10; i++) {
                        if (level.getBlockState(BlockPos.containing(getX(), Math.round(getY()) + i, getZ())).getBlock()
                            == Blocks.LAVA && level.getBlockState(BlockPos.containing(
                            getX(),
                            Math.round(getY()) + i,
                            getZ()
                        )).getFluidState().isSource() && level.getBlockState(BlockPos.containing(
                            getX(),
                            Math.round(getY()) + i + 1,
                            getZ()
                        )).isAir()) {
                            level.playLocalSound(
                                getX(),
                                Math.round(getY()) + i + 0.9,
                                getZ(),
                                SoundEvents.BUCKET_FILL_LAVA,
                                SoundSource.AMBIENT,
                                1.0f,
                                0.8f,
                                true
                            );
                            level.addParticle(
                                ModParticles.INSTANCE.getLAVA_SPLASH(),
                                getX(),
                                Math.round(getY()) + i + 0.9,
                                getZ(),
                                0,
                                0,
                                0
                            );
                            break;
                        }
                    }
                }
            }
        }
    }

}