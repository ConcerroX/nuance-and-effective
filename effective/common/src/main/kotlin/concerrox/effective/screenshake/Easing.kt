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

    abstract fun ease(t: Float, b: Float, c: Float, d: Float): Float

    companion object {
        @JvmField
        val LINEAR = object : Easing() {
            override fun ease(t: Float, b: Float, c: Float, d: Float): Float {
                return c * t / d + b
            }
        }

        @JvmField
        val CIRC_IN_OUT = object : Easing() {
            override fun ease(t: Float, b: Float, c: Float, d: Float): Float {
                var t = t
                if ((d / 2.let { t /= it; t }) < 1) return -c / 2 * (sqrt((1 - t * t).toDouble()).toFloat() - 1) + b
                return c / 2 * (sqrt((1 - (2.let { t -= it; t }) * t).toDouble()).toFloat() + 1) + b
            }
        }
    }
}