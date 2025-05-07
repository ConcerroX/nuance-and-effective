package concerrox.effective.mixin.screenshake;

import concerrox.effective.EffectiveConfig;
import concerrox.effective.screenshake.Easing;
import concerrox.effective.screenshake.PositionedScreenShake;
import concerrox.effective.screenshake.ScreenShakeManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.warden.SonicBoom;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.warden.Warden;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(SonicBoom.class)
public class SonicBoomScreenShakeMixin {

    @Inject(
        method = "tick(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/monster/warden/Warden;J)V",
        at = @At("HEAD")
    )
    protected void keepRunning(ServerLevel level, Warden owner, long gameTime, CallbackInfo ci) {
        if (EffectiveConfig.sonicBoomScreenShake.get() && !owner.getBrain()
            .hasMemoryValue(MemoryModuleType.SONIC_BOOM_SOUND_DELAY) && !owner.getBrain()
            .hasMemoryValue(MemoryModuleType.SONIC_BOOM_SOUND_COOLDOWN)) {
            Objects.requireNonNull(owner.getBrain().getMemoryInternal(MemoryModuleType.ATTACK_TARGET))
                .filter(owner::canTargetEntity)
                .filter(livingEntity -> owner.closerThan(livingEntity, 15.0, 20.0)).ifPresent(livingEntity -> {
                    var boomScreenShake = new PositionedScreenShake(
                        20,
                        owner.position(),
                        20f,
                        25f
                    ).setIntensity(EffectiveConfig.screenShakeIntensity.get().floatValue(), 0.0f, 0.0f);
                    ScreenShakeManager.INSTANCE.addScreenshake(boomScreenShake);
                });
        }
    }

}