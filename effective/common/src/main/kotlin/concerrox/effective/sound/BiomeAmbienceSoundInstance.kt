package concerrox.effective.sound

import concerrox.effective.EffectiveConfig
import concerrox.effective.registry.ModAmbientConditions
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance
import net.minecraft.client.resources.sounds.SoundInstance
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.util.Mth
import kotlin.math.min

class BiomeAmbienceSoundInstance(
    private val player: LocalPlayer,
    ambientSound: SoundEvent,
    private val ambientConditions: ModAmbientConditions.AmbientCondition
) : AbstractTickableSoundInstance(ambientSound, SoundSource.AMBIENT, SoundInstance.createUnseededRandom()) {

    private var transitionTimer = 0.0

    init {
        looping = true
        delay = 0
        volume = 0.001F
        relative = true
    }

    override fun tick() {
        val windVolume = EffectiveConfig.windAmbienceVolume.get() / 100.0
        val waterVolume = EffectiveConfig.waterAmbienceVolume.get() / 100.0
        val foliageVolume = EffectiveConfig.foliageAmbienceVolume.get() / 100.0
        val animalVolume = EffectiveConfig.animalAmbienceVolume.get() / 100.0

        val volumeAdjustor = when (ambientConditions.type) {
            ModAmbientConditions.AmbientCondition.Type.WIND -> windVolume
            ModAmbientConditions.AmbientCondition.Type.ANIMAL -> animalVolume
            ModAmbientConditions.AmbientCondition.Type.FOLIAGE -> foliageVolume
            ModAmbientConditions.AmbientCondition.Type.WATER -> waterVolume
        }

        val world = Minecraft.getInstance().level
        if (world != null && !player.isRemoved && !player.isUnderWater && transitionTimer >= 0 && volumeAdjustor > 0) {
            val shouldPlay = ambientConditions.predicate.shouldPlay(player.level(), player.blockPosition(), player)
            transitionTimer = min(transitionTimer + (if (shouldPlay) 1.0 else -1.0), TRANSITION_TIME)
            volume = Mth.clamp(transitionTimer / TRANSITION_TIME, 0.0, volumeAdjustor).toFloat()
        } else {
            stop()
        }
    }

    companion object {
        private const val TRANSITION_TIME = 100.0
    }
}