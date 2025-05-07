package concerrox.effective.screenshake

import kotlin.math.sqrt

/**
 * The Easing class holds a set of general-purpose motion
 * tweening functions by Robert Penner. This class is
 * essentially a port from Penner's ActionScript utility,
 * with a few added tweaks.
 *
 * @author Robert Penner (functions)
 * @author davedes (java port)
 */
abstract class Easing {

    abstract fun ease(value: Float, min: Float, max: Float, time: Float): Float

    companion object {
        @JvmField
        val LINEAR = object : Easing() {
            override fun ease(value: Float, min: Float, max: Float, time: Float): Float {
                return max * value / time + min
            }
        }

        @JvmField
        val CIRC_OUT = object : Easing() {
            override fun ease(value: Float, min: Float, max: Float, time: Float): Float {
                var t = value
                return max * sqrt((1 - ((t / time - 1).also { t = it }) * t)) + min
            }
        }

    }
}