package concerrox.effective.mixin.screenshake;

import concerrox.effective.EffectiveConfig;
import concerrox.effective.screenshake.Easing;
import concerrox.effective.screenshake.PositionedScreenShake;
import concerrox.effective.screenshake.ScreenShakeManager;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.AbstractDragonSittingPhase;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonSittingAttackingPhase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DragonSittingAttackingPhase.class)
public abstract class EnderDragonRoarScreenShakeMixin extends AbstractDragonSittingPhase {

    public EnderDragonRoarScreenShakeMixin(EnderDragon enderDragon) {
        super(enderDragon);
    }

    @Inject(method = "doClientTick", at = @At("HEAD"))
    public void clientTick(CallbackInfo ci) {
        if (EffectiveConfig.dragonScreenShake.get()) {
            var roarScreenShake = new PositionedScreenShake(
                60,
                dragon.position(),
                20f,
                25f,
                Easing.CIRC_IN_OUT
            ).setIntensity(
                0.0f,
                EffectiveConfig.screenShakeIntensity.get().floatValue(),
                0.0f
            );
            ScreenShakeManager.INSTANCE.addScreenshake(roarScreenShake);
        }
    }
}
