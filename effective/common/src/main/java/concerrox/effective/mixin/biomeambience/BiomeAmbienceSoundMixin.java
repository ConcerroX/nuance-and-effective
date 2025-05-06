package concerrox.effective.mixin.biomeambience;

import concerrox.effective.registry.ModAmbientConditions;
import concerrox.effective.sound.BiomeAmbienceSoundInstance;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(ClientLevel.class)
public class BiomeAmbienceSoundMixin {

    @Final
    @Shadow
    private Minecraft minecraft;

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void effective$playAmbience(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        var clientPlayerEntity = minecraft.player;
        if (clientPlayerEntity != null) {
            for (ModAmbientConditions.AmbientCondition condition : ModAmbientConditions.INSTANCE.getCONDITIONS()) {
                if (minecraft.level != null && condition.getPredicate()
                    .shouldPlay(minecraft.level, minecraft.player.blockPosition(), minecraft.player)) {
                    boolean allow = true;
                    for (TickableSoundInstance tickingSound : ((SoundEngineAccessorMixin) (((SoundManagerAccessorMixin) minecraft.getSoundManager()).getSoundEngine())).getTickingSounds()) {
                        if (tickingSound != null && tickingSound.getLocation()
                            .equals(condition.getEvent().getLocation())) {
                            allow = false;
                            break;
                        }
                    }
                    if (allow) {
                        minecraft.getSoundManager()
                            .play(new BiomeAmbienceSoundInstance(clientPlayerEntity, condition.getEvent(), condition));
                    }
                }
            }
        }
    }

}
