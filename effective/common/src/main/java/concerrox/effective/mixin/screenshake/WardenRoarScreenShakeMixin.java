package concerrox.effective.mixin.screenshake;

import concerrox.effective.EffectiveConfig;
import concerrox.effective.screenshake.Easing;
import concerrox.effective.screenshake.PositionedScreenShake;
import concerrox.effective.screenshake.ScreenShake;
import concerrox.effective.screenshake.ScreenShakeManager;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Warden.class)
public class WardenRoarScreenShakeMixin extends Monster {

    @Unique
    public ScreenShake effective$roarScreenShake;
    @Unique
    public int effective$ticksSinceAnimationStart = 0;

    protected WardenRoarScreenShakeMixin(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        if (EffectiveConfig.wardenScreenShake.get() && getPose().equals(Pose.ROARING)) {
            effective$ticksSinceAnimationStart++;
            if (effective$roarScreenShake == null) {
                if (effective$ticksSinceAnimationStart >= 20) {
                    effective$roarScreenShake = new PositionedScreenShake(
                        70,
                        position(),
                        20f,
                        25f
                    ).setIntensity(
                        0.0f,
                        EffectiveConfig.screenShakeIntensity.get().floatValue(),
                        0.0f
                    );
                    ScreenShakeManager.INSTANCE.addScreenshake(effective$roarScreenShake);
                }
            }
        } else {
            effective$roarScreenShake = null;
            effective$ticksSinceAnimationStart = 0;
        }
    }

}