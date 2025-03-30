package concerrox.effective.extension

import net.minecraft.util.RandomSource

fun RandomSource.nextDoubleOrNegative(): Double {
    return nextDouble() * 2.0 - 1.0
}