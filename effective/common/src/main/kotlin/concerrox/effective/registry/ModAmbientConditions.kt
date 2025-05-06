package concerrox.effective.registry

import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level


object ModAmbientConditions {
    
    val CONDITIONS = HashSet<AmbientCondition>()

    data class AmbientCondition(val event: SoundEvent, val type: Type, val predicate: AmbiencePredicate) {

        fun interface AmbiencePredicate {
            fun shouldPlay(level: Level, pos: BlockPos, player: Player): Boolean
        }

        enum class Type {
            WIND, ANIMAL, FOLIAGE, WATER
        }

    }

}