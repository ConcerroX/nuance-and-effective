package concerrox.effective.screenshake

import net.minecraft.client.Camera
import net.minecraft.util.RandomSource
import net.minecraft.world.phys.Vec3
import kotlin.math.max

class PositionedScreenShake //TODO: make falloff affect duration; the further away a player is the less the screenshake lasts
    (duration: Int, val position: Vec3, private val falloffDistance: Float, private val maxDistance: Float) :
    ScreenShake(duration) {

    override fun updateIntensity(camera: Camera, random: RandomSource): Float {
        val intensity = super.updateIntensity(camera, random)
        val distance = position.distanceTo(camera.position).toFloat()
        if (distance > maxDistance) {
            return 0f
        }
        var distanceMultiplier = 1f
        if (distance > falloffDistance) {
            val remaining = maxDistance - falloffDistance
            val current = distance - falloffDistance
            distanceMultiplier = 1 - current / remaining
        }
        val lookDirection = camera.lookVector
        val directionToScreenshake = position.subtract(camera.position).normalize()
        val angle = max(0.0, lookDirection.dot(directionToScreenshake.toVector3f()).toDouble()).toFloat()
        return ((intensity * distanceMultiplier) + (intensity * angle)) * 0.5f
    }
}