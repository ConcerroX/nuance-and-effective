package concerrox.effective.sound

import net.minecraft.client.Minecraft
import net.minecraft.client.resources.sounds.SimpleSoundInstance
import net.minecraft.client.resources.sounds.SoundInstance
import net.minecraft.client.resources.sounds.TickableSoundInstance
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.util.Mth
import net.minecraft.util.RandomSource
import kotlin.math.sqrt

open class DistancedSoundInstance(
    soundEvent: SoundEvent, soundSource: SoundSource, pitch: Float, blockPos: BlockPos, private val maxDistance: Float
) : SimpleSoundInstance(soundEvent, soundSource, 0.0f, pitch, RANDOM, blockPos), TickableSoundInstance {

    init {
        looping = false
    }

    override fun getAttenuation(): SoundInstance.Attenuation {
        return SoundInstance.Attenuation.NONE
    }

    override fun isStopped(): Boolean {
        return false
    }

    override fun tick() {
        Minecraft.getInstance().player?.let {
            val distance = sqrt(it.position().distanceToSqr(x, y, z))
            volume = Mth.clampedLerp(0.0, 1.0, 1.0 - distance / maxDistance).toFloat()
        }
    }

    companion object {
        private val RANDOM = RandomSource.create()

        fun ambient(
            soundEvent: SoundEvent, pitch: Float, blockPos: BlockPos, maxDistance: Float
        ): DistancedSoundInstance {
            return DistancedSoundInstance(soundEvent, SoundSource.AMBIENT, pitch, blockPos, maxDistance)
        }
    }
}