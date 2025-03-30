package concerrox.effective.util

import java.awt.Color

internal val Color.redFloat: Float
    get() = red / 255.0f

internal val Color.greenFloat: Float
    get() = green / 255.0f

internal val Color.blueFloat: Float
    get() = blue / 255.0f

internal val Color.alphaFloat: Float
    get() = alpha / 255.0f
