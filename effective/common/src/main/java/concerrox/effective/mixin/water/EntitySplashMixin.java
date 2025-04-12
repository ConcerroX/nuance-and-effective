package concerrox.effective.mixin.water;

import concerrox.effective.EffectiveConfig;
import concerrox.effective.effect.SplashSpawner;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntitySplashMixin {
    @Shadow
    private Level level;

    @Inject(method = "doWaterSplashEffect", at = @At("TAIL"))
    protected void onSwimmingStart(CallbackInfo callbackInfo) {
        if (level.isClientSide && EffectiveConfig.isSplashesEnabled.get()) {
            SplashSpawner.INSTANCE.trySpawnSplash((Entity) (Object) this);
        }
    }
}