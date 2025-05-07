@file:JvmName("RandomSourceUtils")

package concerrox.effective.util

import net.minecraft.util.RandomSource

fun RandomSource.nextDoubleOrNegative(): Double {
    return nextDouble() * 2.0 - 1.0
}

fun RandomSource.nextFloatOrNegative(): Float {
    return nextFloat() * 2F - 1F
}

