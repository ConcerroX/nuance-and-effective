package concerrox.effective.registry

import concerrox.effective.Effective
import net.minecraft.sounds.SoundEvent

object ModSounds {
    val SOUNDS = mutableListOf<SoundEvent>()

    val AMBIENT_CASCADE by register("ambient.cascade")

    private fun register(id: String): Lazy<SoundEvent> {
        val sound = SoundEvent.createVariableRangeEvent(Effective.id(id))
        val lazyType = lazy { sound }
        SOUNDS += sound
        return lazyType
    }
}
