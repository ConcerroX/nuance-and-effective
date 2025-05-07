package concerrox.effective.util

import net.minecraft.world.entity.animal.allay.Allay
import kotlin.math.absoluteValue

object AllayUtils {

    fun isGoingFast(allayEntity: Allay): Boolean {
        val velocity = allayEntity.deltaMovement
        val speedRequired = 0.1f
        return velocity.x.absoluteValue >= speedRequired || velocity.y.absoluteValue >= speedRequired || velocity.z.absoluteValue >= speedRequired
    }

}