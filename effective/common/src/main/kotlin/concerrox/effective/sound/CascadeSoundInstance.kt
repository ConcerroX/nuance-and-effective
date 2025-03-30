package concerrox.effective.sound

import concerrox.effective.EffectiveConfig
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.util.Mth

class CascadeSoundInstance(
    soundEvent: SoundEvent, soundSource: SoundSource, pitch: Float, blockPos: BlockPos, maxDistance: Float
) : DistancedSoundInstance(soundEvent, soundSource, pitch, blockPos, maxDistance) {

    override fun tick() {
        super.tick()
        val volumeAdjustor = EffectiveConfig.cascadeSoundsVolume.get().toFloat() / 100F * 2.5F
        volume = Mth.clampedLerp(0F, volumeAdjustor, volume)
    }

    companion object {
        fun ambient(
            soundEvent: SoundEvent, pitch: Float, blockPos: BlockPos, maxDistance: Float
        ): CascadeSoundInstance {
            return CascadeSoundInstance(soundEvent, SoundSource.AMBIENT, pitch, blockPos, maxDistance)
        }
    }
}