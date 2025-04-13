package concerrox.effective.mixin.screenshake;

import concerrox.effective.EffectiveConfig;
import concerrox.effective.screenshake.ScreenShakeManager;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public class CameraScreenShakeMixin {

    @Inject(method = "setup", at = @At("RETURN"))
    private void screenShake(
        BlockGetter level, Entity entity, boolean detached, boolean thirdPersonReverse, float partialTick,
        CallbackInfo ci
    ) {
        if (EffectiveConfig.screenShakeIntensity.get() > 0) {
            ScreenShakeManager.INSTANCE.cameraTick((Camera) (Object) this);
        }
    }

}