package concerrox.effective.mixin.screenshake;

import concerrox.effective.EffectiveConfig;
import concerrox.effective.screenshake.Easing;
import concerrox.effective.screenshake.PositionedScreenShake;
import concerrox.effective.screenshake.ScreenShakeManager;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Ravager.class)
public class RavagerRoarScreenShakeMixin extends Monster {

    protected RavagerRoarScreenShakeMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "roar", at = @At("HEAD"))
    public void roar(CallbackInfo ci) {
        if (EffectiveConfig.ravagerScreenShake.get()) {
            var roarScreenShake = new PositionedScreenShake(10, position(), 20f, 25f, Easing.CIRC_IN_OUT).setIntensity(0.0f,
                EffectiveConfig.screenShakeIntensity.get().floatValue(),
                0.0f
            );
            ScreenShakeManager.INSTANCE.addScreenshake(roarScreenShake);
        }
    }
}